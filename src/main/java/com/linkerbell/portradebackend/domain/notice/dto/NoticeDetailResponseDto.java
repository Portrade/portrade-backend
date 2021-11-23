package com.linkerbell.portradebackend.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeDetailResponseDto {

    private final Long id;
    private final String creator;
    private final String title;
    private final String content;
    private final int viewCount;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;
    private final NoticeResponseDto next;
    private final NoticeResponseDto prev;

    @Builder
    public NoticeDetailResponseDto(Long id, String creator, String title, String content, int viewCount, LocalDateTime createdDate, LocalDateTime lastModifiedDate, NoticeResponseDto next, NoticeResponseDto prev) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.next = next;
        this.prev = prev;
    }
}
