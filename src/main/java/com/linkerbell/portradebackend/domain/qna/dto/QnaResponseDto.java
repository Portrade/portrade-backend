package com.linkerbell.portradebackend.domain.qna.dto;

import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QnaResponseDto {

    private final Long id;
    private final String title;
    private final Boolean isPublic;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    private QnaResponseDto(Long id, String title, Boolean isPublic, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.isPublic = isPublic;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static QnaResponseDto of(Qna qna) {
        return QnaResponseDto.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .isPublic(qna.isPublic())
                .createdDate(qna.getCreatedDate())
                .lastModifiedDate(qna.getLastModifiedDate())
                .build();
    }
}
