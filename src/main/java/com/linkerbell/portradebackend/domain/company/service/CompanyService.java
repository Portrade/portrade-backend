package com.linkerbell.portradebackend.domain.company.service;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.company.dto.*;
import com.linkerbell.portradebackend.domain.company.repository.CompanyRepository;
import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import com.linkerbell.portradebackend.domain.recruitment.repository.RecruitmentRepository;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.DuplicatedValueException;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public IdResponseDto createCompany(CompanyRequestDto companyRequestDto) {
        if (companyRepository.findByNameAndCeo(companyRequestDto.getName(), companyRequestDto.getCeo()).orElse(null) != null) {
            throw new DuplicatedValueException(ErrorCode.DUPLICATED_COMPANY);
        }

        Company company = companyRequestDto.toEntity();
        companyRepository.save(company);
        return new IdResponseDto(company.getId());
    }

    public CompaniesResponseDto getCompanies(int page, int size, String name) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Company> companyPage = companyRepository.findAllByNameContainingIgnoreCase(pageable, name);

        List<CompanyResponseDto> companyResponseDtos = companyPage.stream()
                .map(CompanyResponseDto::of)
                .collect(Collectors.toList());

        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(companyPage.getTotalPages())
                .totalElement(companyPage.getTotalElements())
                .build();

        return CompaniesResponseDto.builder()
                .page(pageResponseDto)
                .companies(companyResponseDtos)
                .build();
    }

    public CompanyDetailResponseDto getCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_COMPANY));
        return CompanyDetailResponseDto.of(company);
    }

    @Transactional
    public IdResponseDto updateCompany(CompanyRequestDto companyRequestDto, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_COMPANY));

        company.updateCompany(companyRequestDto);
        companyRepository.save(company);

        return new IdResponseDto(companyId);
    }

    public RecruitmentsResponseDto getRecruitments(int page, int size, Long companyId) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Recruitment> recruitmentPage = recruitmentRepository.findAllByCompany_Id(pageable, companyId);

        List<RecruitmentResponseDto> recruitmentResponseDtos = recruitmentPage.stream()
                .map(RecruitmentResponseDto::of)
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(recruitmentPage.getTotalPages())
                .totalElement(recruitmentPage.getTotalElements())
                .build();

        return RecruitmentsResponseDto.builder()
                .page(pageResponseDto)
                .recruitments(recruitmentResponseDtos)
                .build();
    }

    @Transactional
    public void deleteCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_COMPANY));

        companyRepository.delete(company);
    }

}
