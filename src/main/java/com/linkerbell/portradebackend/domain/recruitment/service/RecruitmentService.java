package com.linkerbell.portradebackend.domain.recruitment.service;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.company.dto.CompanyDetailResponseDto;
import com.linkerbell.portradebackend.domain.company.dto.RecruitmentsResponseDto;
import com.linkerbell.portradebackend.domain.company.repository.CompanyRepository;
import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import com.linkerbell.portradebackend.domain.recruitment.dto.RecruitmentDetailResponseDto;
import com.linkerbell.portradebackend.domain.recruitment.dto.RecruitmentRequestDto;
import com.linkerbell.portradebackend.domain.recruitment.dto.RecruitmentResponseDto;
import com.linkerbell.portradebackend.domain.recruitment.repository.RecruitmentRepository;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
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
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public IdResponseDto createRecruitment(Long companyId, RecruitmentRequestDto recruitmentRequestDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_COMPANY));

        Recruitment recruitment = recruitmentRequestDto.toEntity(company);
        recruitmentRepository.save(recruitment);
        return new IdResponseDto(recruitment.getId());
    }

    public RecruitmentsResponseDto getRecruitments(int page, int size, String area, String job, String title) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Recruitment> recruitmentPage = recruitmentRepository.findAllByTitleContainingAndAddressContainingAndCareerContaining(pageable, title, area, job);

        List<com.linkerbell.portradebackend.domain.company.dto.RecruitmentResponseDto> recruitmentResponseDtos = recruitmentPage.stream()
                .map(com.linkerbell.portradebackend.domain.company.dto.RecruitmentResponseDto::of)
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
    public RecruitmentDetailResponseDto getRecruitment(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_RECRUITMENT));

        recruitment.addViewCount();

        Company company = companyRepository.findById(recruitment.getCompany().getId())
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_COMPANY));

        CompanyDetailResponseDto companyResponseDto = CompanyDetailResponseDto.of(company);
        RecruitmentResponseDto recruitmentResponseDto = RecruitmentResponseDto.builder()
                .id(recruitment.getId())
                .company(recruitment.getCompany().getName())
                .logo(recruitment.getLogo())
                .title(recruitment.getTitle())
                .viewCount(recruitment.getViewCount())
                .career(recruitment.getCareer())
                .education(recruitment.getEducation())
                .pay(recruitment.getPay())
                .url(recruitment.getUrl())
                .address(recruitment.getAddress())
                .category(recruitment.getCategory())
                .createdDate(recruitment.getCreatedDate())
                .lastModifiedDate(recruitment.getLastModifiedDate())
                .build();

        return RecruitmentDetailResponseDto.builder()
                .company(companyResponseDto)
                .recruitment(recruitmentResponseDto)
                .build();
    }

    @Transactional
    public IdResponseDto editRecruitment(Long recruitmentId, RecruitmentRequestDto recruitmentRequestDto) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_RECRUITMENT));

        recruitment.update(recruitmentRequestDto);

        return new IdResponseDto(recruitment.getId());
    }

    @Transactional
    public void deleteRecruitment(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_RECRUITMENT));

        recruitmentRepository.delete(recruitment);
    }
}
