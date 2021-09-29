package com.linkerbell.portradebackend.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QnaDetailResponseDto {
    private final QnaCurDetailResponseDto cur;
    private final QnaNextDetailResponseDto next;
    private final QnaNextDetailResponseDto prev;

    @Builder
    public QnaDetailResponseDto(QnaCurDetailResponseDto cur, QnaNextDetailResponseDto next, QnaNextDetailResponseDto prev) {
        this.cur = cur;
        this.next = next;
        this.prev = prev;
    }
}
