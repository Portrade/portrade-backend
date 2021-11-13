package com.linkerbell.portradebackend.domain.company.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecruitmentsResponseDto {

    private final PageResponseDto page;
    private final List<RecruitmentResponseDto> recruitments;

    @Builder
    public RecruitmentsResponseDto(PageResponseDto page, List<RecruitmentResponseDto> recruitments) {
        this.page = page;
        this.recruitments = recruitments;
    }
}
