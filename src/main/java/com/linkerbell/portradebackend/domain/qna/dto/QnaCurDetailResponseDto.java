package com.linkerbell.portradebackend.domain.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class QnaCurDetailResponseDto {
    private final Long id;
    private final String creator;
    private final String title;
    private final String content;
    private final boolean isPublic;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime lastModifiedDate;

    @Builder
    public QnaCurDetailResponseDto(Long id, String creator, String title, String content, boolean isPublic, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static QnaCurDetailResponseDto toDto(Qna qna) {
        return QnaCurDetailResponseDto.builder()
                .id(qna.getId())
                .creator(qna.name())
                .title(qna.getTitle())
                .content(qna.getContent())
                .isPublic(qna.isPublic())
                .createdDate(qna.getCreatedDate())
                .lastModifiedDate(qna.getLastModifiedDate())
                .build();
    }
}
