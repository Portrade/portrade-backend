package com.linkerbell.portradebackend.domain.company.service;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.company.dto.*;
import com.linkerbell.portradebackend.domain.company.repository.CompanyRepository;
import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import com.linkerbell.portradebackend.domain.recruitment.repository.RecruitmentRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.DuplicatedValueException;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public CreateResponseDto createCompany(CreateCompanyRequestDto companyRequestDto, User user) {
        //이미 존재하는지 확인 - 일단 기업명과 ceo 가 동일하면 이미 존재하는 기업으로 간주했습니다.
        Optional<Company> savedCompany = companyRepository.findByNameAndCeo(companyRequestDto.getName(), companyRequestDto.getCeo());
        if (savedCompany.isPresent())
            throw new DuplicatedValueException(ErrorCode.DUPLICATED_COMPANY);

        Company company = companyRequestDto.toEntity(user);
        companyRepository.save(company);
        return new CreateResponseDto(company.getId());
    }

    public CompanyDetailResponseDto getCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_COMPANY));
        CompanyDetailResponseDto companyDetailResponseDto = CompanyDetailResponseDto.of(company);
        return companyDetailResponseDto;
    }

    @Transactional
    public void updateCompany(CompanyRequestDto companyRequestDto, Long companyId, User user) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_COMPANY));
        if (!user.equals(company.getUser()))
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);

        company.updateCompany(companyRequestDto);
        companyRepository.save(company);
    }

    public CompanyRecruitmentResponseDto getRecruitments(int page, int size, Long companyId) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<Recruitment> recruitmentsPage = recruitmentRepository.findAllByCompany_Id(pageable, companyId);
        List<RecruitmentResponseDto> recruitments = recruitmentsPage.stream()
                .map(recruitment -> RecruitmentResponseDto.builder()
                        .id(recruitment.getId())
                        .logo(recruitment.getLogo())
                        .title(recruitment.getTitle())
                        .build())
                .collect(Collectors.toList());

        return CompanyRecruitmentResponseDto.builder()
                .recruitments(recruitments)
                .maxPage(recruitmentsPage.getTotalPages())
                .build();
    }

    @Transactional
    public void deleteCompany(Long companyId, User user) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_COMPANY));
        if (!user.equals(company.getUser()))
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);

        companyRepository.delete(company);
    }
}
