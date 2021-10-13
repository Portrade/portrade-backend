package com.linkerbell.portradebackend.domain.save.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SavedRecruitmentResponseDto {
    private final String logo;
    private final String title;
    private final String companyName;
    private final String career;
    private final String education;
    private final String address;
    private final LocalDateTime lastModifiedDate;

    @Builder
    public SavedRecruitmentResponseDto(String logo, String title, String companyName, String career, String education, String address, LocalDateTime lastModifiedDate) {
        this.logo = logo;
        this.title = title;
        this.companyName = companyName;
        this.career = career;
        this.education = education;
        this.address = address;
        this.lastModifiedDate = lastModifiedDate;
    }
}
