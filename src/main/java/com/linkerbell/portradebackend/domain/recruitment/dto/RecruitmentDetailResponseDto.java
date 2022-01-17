package com.linkerbell.portradebackend.domain.recruitment.dto;

import com.linkerbell.portradebackend.domain.company.dto.CompanyDetailResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecruitmentDetailResponseDto {

    private final CompanyDetailResponseDto company;
    private final RecruitmentResponseDto recruitment;

    @Builder
    public RecruitmentDetailResponseDto(CompanyDetailResponseDto company, RecruitmentResponseDto recruitment) {
        this.company = company;
        this.recruitment = recruitment;
    }
}
