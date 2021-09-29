package com.linkerbell.portradebackend.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class QnasResponseDto {
    private final List<QnaResponseDto> qnas;
    private final int maxPage;

    @Builder
    public QnasResponseDto(List<QnaResponseDto> qnas, int maxPage) {
        this.qnas = qnas;
        this.maxPage = maxPage;
    }
}
