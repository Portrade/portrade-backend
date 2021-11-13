package com.linkerbell.portradebackend.domain.portfolio.dto;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.global.common.File;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PortfolioDetailResponseDto {

    private final Long id;
    private final String creator;
    private final String title;
    private final String description;
    private final String category;
    private final Boolean isPublic;

    private final int viewCount;
    private final int likeCount;
    private final int commentCount;

    private final File mainImageFile;
    private final List<File> contentFiles;

    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    private PortfolioDetailResponseDto(Long id, String creator, String title, String description, String category, Boolean isPublic, int viewCount, int likeCount, int commentCount, LocalDateTime createdDate, LocalDateTime lastModifiedDate, File mainImageFile, List<File> contentFiles) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isPublic = isPublic;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.mainImageFile = mainImageFile;
        this.contentFiles = contentFiles;
    }

    public static PortfolioDetailResponseDto of(Portfolio portfolio) {
        return PortfolioDetailResponseDto.builder()
                .id(portfolio.getId())
                .creator(portfolio.getCreator().getUsername())
                .title(portfolio.getTitle())
                .description(portfolio.getDescription())
                .category(portfolio.getCategory())
                .isPublic(portfolio.isPublic())
                .viewCount(portfolio.getViewCount())
                .likeCount(portfolio.getLikeCount())
                .commentCount(portfolio.getCommentCount())
                .mainImageFile(portfolio.getMainImageFile())
                .contentFiles(portfolio.getContentFiles())
                .createdDate(portfolio.getCreatedDate())
                .lastModifiedDate(portfolio.getLastModifiedDate())
                .build();
    }
}
