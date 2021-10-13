package com.linkerbell.portradebackend.domain.save.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SavedPortfolioResponseDto {
    private final Long id;
    private final String title;
    private final LocalDateTime createdDate;

    @Builder
    public SavedPortfolioResponseDto(Long id, String title, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
    }
}
