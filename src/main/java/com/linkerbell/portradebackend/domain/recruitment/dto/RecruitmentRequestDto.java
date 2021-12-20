package com.linkerbell.portradebackend.domain.recruitment.dto;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentRequestDto {

    private String logo;
    @NotNull(message = "NULL_RECRUITMENT_TITLE")
    private String title;
    @NotNull(message = "NULL_RECRUITMENT_CAREER")
    private String career;
    @NotNull(message = "NULL_RECRUITMENT_EDUCATION")
    private String education;
    @NotNull(message = "NULL_RECRUITMENT_WORKTYPE")
    private String workType;
    @NotNull(message = "NULL_RECRUITMENT_PAY")
    private String pay;
    @NotNull(message = "NULL_RECRUITMENT_ADDRESS")
    private String address;
    @NotNull(message = "NULL_RECRUITMENT_CATEGORY")
    private String category;

    @Builder
    public RecruitmentRequestDto(String logo, String title, String career, String education, String workType, String pay, String address, String category) {
        this.logo = logo;
        this.title = title;
        this.career = career;
        this.education = education;
        this.workType = workType;
        this.pay = pay;
        this.address = address;
        this.category = category;
    }

    public Recruitment toEntity(Company company) {
        return Recruitment.builder()
                .logo(logo)
                .title(title)
                .career(career)
                .education(education)
                .workType(workType)
                .pay(pay)
                .address(address)
                .category(category)
                .company(company)
                .build();
    }
}


