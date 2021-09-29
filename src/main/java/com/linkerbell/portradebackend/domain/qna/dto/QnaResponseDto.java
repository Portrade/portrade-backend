package com.linkerbell.portradebackend.domain.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QnaResponseDto {
    private final Long id;
    private final String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime createdDate;

    public QnaResponseDto(Long id, String title, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
    }
}
