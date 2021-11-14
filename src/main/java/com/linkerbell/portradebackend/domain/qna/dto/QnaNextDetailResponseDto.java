package com.linkerbell.portradebackend.domain.qna.dto;

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
    private final LocalDateTime createdDate;

    @Builder
    private QnaNextDetailResponseDto(Long id, String creator, String title, LocalDateTime createdDate) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.createdDate = createdDate;
    }

    public static QnaNextDetailResponseDto of(Qna qna) {
        if(Objects.isNull(qna))
            return null;

        return QnaNextDetailResponseDto.builder()
                .id(qna.getId())
                .creator(qna.getCreatorName())
                .title(qna.getTitle())
                .createdDate(qna.getCreatedDate())
                .build();
    }
}
