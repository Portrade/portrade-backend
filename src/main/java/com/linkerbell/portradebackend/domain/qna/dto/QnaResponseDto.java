package com.linkerbell.portradebackend.domain.qna.dto;

import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QnaResponseDto {

    private final Long id;
    private final String title;
    private final LocalDateTime createdDate;

    @Builder
    public QnaResponseDto(Long id, String title, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
    }

    public static QnaResponseDto of(Qna qna) {
        return QnaResponseDto.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .createdDate(qna.getCreatedDate())
                .build();
    }
}
