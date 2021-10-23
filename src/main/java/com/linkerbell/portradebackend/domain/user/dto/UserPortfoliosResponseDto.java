package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserPortfoliosResponseDto {
    private final List<UserPortfolioResponseDto> portfolios;
    private final int maxPage;

    @Builder
    public UserPortfoliosResponseDto(List<UserPortfolioResponseDto> portfolios, int maxPage) {
        this.portfolios = portfolios;
        this.maxPage = maxPage;
    }
}
