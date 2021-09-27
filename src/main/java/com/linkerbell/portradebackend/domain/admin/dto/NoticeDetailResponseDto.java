package com.linkerbell.portradebackend.domain.admin.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeDetailResponseDto {

    private Long id;
    private String creator;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    public NoticeDetailResponseDto(Long id, String creator, String title, String content, int viewCount, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
