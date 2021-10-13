package com.linkerbell.portradebackend.domain.save.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SavedRecruitmentsResponseDto {
    private final List<SavedRecruitmentResponseDto> recruitments;
    private final int maxPage;

    @Builder
    public SavedRecruitmentsResponseDto(List<SavedRecruitmentResponseDto> recruitments, int maxPage) {
        this.recruitments = recruitments;
        this.maxPage = maxPage;
    }
}
