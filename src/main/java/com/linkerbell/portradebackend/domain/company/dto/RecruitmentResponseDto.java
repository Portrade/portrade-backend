package com.linkerbell.portradebackend.domain.company.dto;

import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecruitmentResponseDto {

    private final Long id;
    private final String logo;
    private final String title;
    private final String companyName;
    private final String career;
    private final String category;
    private final String education;
    private final String address;
    private final LocalDateTime lastModifiedDate;

    @Builder
    private RecruitmentResponseDto(Long id, String logo, String title, String companyName, String career, String category, String education, String address, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.logo = logo;
        this.title = title;
        this.companyName = companyName;
        this.career = career;
        this.category = category;
        this.education = education;
        this.address = address;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static RecruitmentResponseDto of(Recruitment recruitment) {
        return RecruitmentResponseDto.builder()
                .id(recruitment.getId())
                .logo(recruitment.getLogo())
                .title(recruitment.getTitle())
                .companyName(recruitment.getCompany().getName())
                .career(recruitment.getCareer())
                .category(recruitment.getCategory())
                .education(recruitment.getEducation())
                .address(recruitment.getAddress())
                .lastModifiedDate(recruitment.getLastModifiedDate())
                .build();
    }
}
