package com.linkerbell.portradebackend.domain.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QnaCurDetailResponseDto {
    private final Long id;
    private final String creator;
    private final String title;
    private final String content;
    private final boolean secret;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime lastModifiedDate;

    @Builder
    public QnaCurDetailResponseDto(Long id, String creator, String title, String content, boolean secret, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.content = content;
        this.secret = secret;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
