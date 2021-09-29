package com.linkerbell.portradebackend.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QnaNextDetailResponseDto {
    private final Long id;
    private final String creator;
    private final String title;
    private final LocalDateTime createdDate;

    @Builder
    public QnaNextDetailResponseDto(Long id, String creator, String title, LocalDateTime createdDate) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.createdDate = createdDate;
    }
}
