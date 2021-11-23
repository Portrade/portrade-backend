package com.linkerbell.portradebackend.domain.company.dto;

import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecruitmentResponseDto {

    private final Long id;
    private final String logo;
    private final String title;

    @Builder
    private RecruitmentResponseDto(Long id, String logo, String title) {
        this.id = id;
        this.logo = logo;
        this.title = title;
    }

    public static RecruitmentResponseDto of(Recruitment recruitment) {
        return RecruitmentResponseDto.builder()
                .id(recruitment.getId())
                .logo(recruitment.getLogo())
                .title(recruitment.getTitle())
                .build();
    }
}
