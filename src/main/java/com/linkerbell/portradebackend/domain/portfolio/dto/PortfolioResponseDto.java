package com.linkerbell.portradebackend.domain.portfolio.dto;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PortfolioResponseDto {

    private final Long id;
    private final String creator;
    private final String title;
    private final Boolean isPublic;
    private final int viewCount;
    private final String mainImageUrl;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    private PortfolioResponseDto(Long id, String creator, String title, Boolean isPublic, int viewCount, String mainImageUrl, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.isPublic = isPublic;
        this.viewCount = viewCount;
        this.mainImageUrl = mainImageUrl;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static PortfolioResponseDto of(Portfolio portfolio) {
        return PortfolioResponseDto.builder()
                .id(portfolio.getId())
                .creator(portfolio.getCreator().getUsername())
                .title(portfolio.getTitle())
                .isPublic(portfolio.isPublic())
                .viewCount(portfolio.getViewCount())
                .mainImageUrl(portfolio.getMainImageFile().getUrl())
                .createdDate(portfolio.getCreatedDate())
                .lastModifiedDate(portfolio.getLastModifiedDate())
                .build();
    }
}
