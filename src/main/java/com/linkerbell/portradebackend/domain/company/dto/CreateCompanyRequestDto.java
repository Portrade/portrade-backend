package com.linkerbell.portradebackend.domain.company.dto;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCompanyRequestDto {
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
    public CreateCompanyRequestDto(String name, String form, String industry, String sales, String homepage, String memberCount, String address, String ceo, String foundingDate) {
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

    public Company toEntity(User user) {
        return Company.builder()
                .name(name)
                .user(user)
                .form(form)
                .industry(industry)
                .sales(sales)
                .homepage(homepage)
                .memberCount(memberCount)
                .address(address)
                .ceo(ceo)
                .foundingDate(foundingDate)
                .build();
    }
}
