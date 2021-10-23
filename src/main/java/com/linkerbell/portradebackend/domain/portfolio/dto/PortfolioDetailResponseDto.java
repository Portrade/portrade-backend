package com.linkerbell.portradebackend.domain.portfolio.dto;

import com.linkerbell.portradebackend.domain.file.dto.FileResponseDto;
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

    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    private final FileResponseDto mainImage;
    private final List<FileResponseDto> contentFiles;

    @Builder
    public PortfolioDetailResponseDto(Long id, String creator, String title, String description, String category, Boolean isPublic, int viewCount, int likeCount, int commentCount, LocalDateTime createdDate, LocalDateTime lastModifiedDate, FileResponseDto mainImage, List<FileResponseDto> contentFiles) {
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
        this.mainImage = mainImage;
        this.contentFiles = contentFiles;
    }
}
