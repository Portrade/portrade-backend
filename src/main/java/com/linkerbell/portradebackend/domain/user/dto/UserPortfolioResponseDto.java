package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserPortfolioResponseDto {
    private final Long id;
    private final String title;
    private final LocalDateTime createdDate;

    public UserPortfolioResponseDto(Long id, String title, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
    }
}
