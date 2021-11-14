package com.linkerbell.portradebackend.domain.portfolio.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PortfoliosResponseDto {

    private final PageResponseDto page;
    private final List<PortfolioResponseDto> portfolios;

    @Builder
    public PortfoliosResponseDto(PageResponseDto page, List<PortfolioResponseDto> portfolios) {
        this.page = page;
        this.portfolios = portfolios;
    }
}
