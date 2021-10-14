package com.linkerbell.portradebackend.domain.company.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CompanyRecruitmentResponseDto {
    private final int maxPage;
    private final List<RecruitmentResponseDto> recruitments;

    @Builder
    public CompanyRecruitmentResponseDto(int maxPage, List<RecruitmentResponseDto> recruitments) {
        this.maxPage = maxPage;
        this.recruitments = recruitments;
    }
}
