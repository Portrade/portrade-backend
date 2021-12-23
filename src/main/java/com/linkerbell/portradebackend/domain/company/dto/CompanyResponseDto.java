package com.linkerbell.portradebackend.domain.company.dto;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CompanyResponseDto {

    private final Long id;
    private final String name;

    @Builder
    private CompanyResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CompanyResponseDto of(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .build();
    }
}
