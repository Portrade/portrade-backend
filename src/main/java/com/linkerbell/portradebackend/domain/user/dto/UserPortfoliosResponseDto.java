package com.linkerbell.portradebackend.domain.user.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserPortfoliosResponseDto {

    private final PageResponseDto page;
    private final List<UserPortfolioResponseDto> portfolios;

    @Builder
    public UserPortfoliosResponseDto(PageResponseDto page, List<UserPortfolioResponseDto> portfolios) {
        this.page = page;
        this.portfolios = portfolios;
    }
}
