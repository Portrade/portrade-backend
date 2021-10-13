package com.linkerbell.portradebackend.domain.save.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

//TODO 포트폴리오에서 겹치는 dto 있으면 합치면 좋을거 같아요
@Getter
public class SavedPortfoliosResponseDto {
    private final List<SavedPortfolioResponseDto> portfolios;
    private final int maxPage;

    @Builder
    public SavedPortfoliosResponseDto(List<SavedPortfolioResponseDto> portfolios, int maxPage) {
        this.portfolios = portfolios;
        this.maxPage = maxPage;
    }
}
