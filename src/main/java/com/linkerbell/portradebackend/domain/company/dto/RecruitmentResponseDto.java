package com.linkerbell.portradebackend.domain.company.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RecruitmentResponseDto {
    private final Long id;
    private final String logo;
    private final String title;

    @Builder
    public RecruitmentResponseDto(Long id, String logo, String title) {
        this.id = id;
        this.logo = logo;
        this.title = title;
    }
}
