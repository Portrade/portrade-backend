package com.linkerbell.portradebackend.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QnaDetailResponseDto {

    private final Long id;
    private final String creator;
    private final String title;
    private final String content;
    private final Boolean isPublic;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    private final QnaNextDetailResponseDto next;
    private final QnaNextDetailResponseDto prev;

    @Builder
    public QnaDetailResponseDto(Long id, String creator, String title, String content, Boolean isPublic, LocalDateTime createdDate, LocalDateTime lastModifiedDate, QnaNextDetailResponseDto next, QnaNextDetailResponseDto prev) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.next = next;
        this.prev = prev;
    }
}
