package com.linkerbell.portradebackend.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QnaDetailResponseDto {
    private Long id;
    private String creator;
    private String title;
    private String content;
    private boolean secret;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    private final QnaNextDetailResponseDto next;
    private final QnaNextDetailResponseDto prev;

    @Builder
    public QnaDetailResponseDto(Long id, String creator, String title, String content, boolean secret, LocalDateTime createdDate, LocalDateTime lastModifiedDate, QnaNextDetailResponseDto next, QnaNextDetailResponseDto prev) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.content = content;
        this.secret = secret;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.next = next;
        this.prev = prev;
    }
}
