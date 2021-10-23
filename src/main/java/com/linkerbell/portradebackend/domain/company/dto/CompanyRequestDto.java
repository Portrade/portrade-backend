package com.linkerbell.portradebackend.domain.company.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyRequestDto {
    @NotNull(message = "NULL_COMPANY_NAME")
    private String name;
    @NotNull(message = "NULL_COMPANY_FORM")
    private String form;
    @NotNull(message = "NULL_COMPANY_INDUSTRY")
    private String industry;
    @NotNull(message = "NULL_COMPANY_SALES")
    private String sales;
    @NotNull(message = "NULL_COMPANY_HOMEPAGE")
    private String homepage;

    private String memberCount;
    @NotNull(message = "NULL_COMPANY_ADDRESS")
    private String address;
    @NotNull(message = "NULL_COMPANY_CEO")
    private String ceo;

    private String foundingDate;

    @Builder
    public CompanyRequestDto(String name, String form, String industry, String sales, String homepage, String memberCount, String address, String ceo, String foundingDate) {
        this.name = name;
        this.form = form;
        this.industry = industry;
        this.sales = sales;
        this.homepage = homepage;
        this.memberCount = memberCount;
        this.address = address;
        this.ceo = ceo;
        this.foundingDate = foundingDate;
    }
}
