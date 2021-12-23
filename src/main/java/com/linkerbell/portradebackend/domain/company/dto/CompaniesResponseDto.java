package com.linkerbell.portradebackend.domain.company.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CompaniesResponseDto {

    private final PageResponseDto page;
    private final List<CompanyResponseDto> companies;

    @Builder
    public CompaniesResponseDto(PageResponseDto page, List<CompanyResponseDto> companies) {
        this.page = page;
        this.companies = companies;
    }
}
