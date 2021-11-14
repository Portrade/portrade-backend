package com.linkerbell.portradebackend.domain.company.dto;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CompanyDetailResponseDto {
    private final Long id;
    private final String name;
    private final String form;
    private final String industry;
    private final String sales;
    private final String homepage;
    private final String memberCount;
    private final String address;
    private final String ceo;
    private final String foundingDate;

    @Builder
    private CompanyDetailResponseDto(Long id, String name, String form, String industry, String sales, String homepage, String memberCount, String address, String ceo, String foundingDate) {
        this.id = id;
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

    public static CompanyDetailResponseDto of(Company company) {
        return CompanyDetailResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .form(company.getForm())
                .industry(company.getIndustry())
                .sales(company.getSales())
                .homepage(company.getHomepage())
                .memberCount(company.getMemberCount())
                .address(company.getAddress())
                .ceo(company.getCeo())
                .foundingDate(company.getFoundingDate())
                .build();
    }
}

