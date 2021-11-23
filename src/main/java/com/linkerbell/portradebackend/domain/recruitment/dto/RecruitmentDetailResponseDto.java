package com.linkerbell.portradebackend.domain.recruitment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecruitmentDetailResponseDto {

    private final Long id;
    private final String company;
    private final String logo;
    private final String title;
    private final int viewCount;
    private final String career;
    private final String education;
    private final String pay;
    private final String address;
    private final String category;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    public RecruitmentDetailResponseDto(Long id, String company, String logo, String title, int viewCount, String career, String education, String pay, String address, String category, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.company = company;
        this.logo = logo;
        this.title = title;
        this.viewCount = viewCount;
        this.career = career;
        this.education = education;
        this.pay = pay;
        this.address = address;
        this.category = category;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
