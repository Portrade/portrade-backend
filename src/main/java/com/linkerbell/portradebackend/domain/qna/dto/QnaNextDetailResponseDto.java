package com.linkerbell.portradebackend.domain.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class QnaNextDetailResponseDto {
    private final Long id;
    private final String creator;
    private final String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime createdDate;

    @Builder
    public QnaNextDetailResponseDto(Long id, String creator, String title, LocalDateTime createdDate) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.createdDate = createdDate;
    }

    public static QnaNextDetailResponseDto toDto(Qna qna) {
        if(Objects.isNull(qna))
            return null;

        return QnaNextDetailResponseDto.builder()
                .id(qna.getId())
                .creator(qna.name())
                .title(qna.getTitle())
                .createdDate(qna.getCreatedDate())
                .build();
    }
}
