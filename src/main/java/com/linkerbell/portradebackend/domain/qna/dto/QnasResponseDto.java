package com.linkerbell.portradebackend.domain.qna.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class QnasResponseDto {

    private final PageResponseDto page;
    private final List<QnaResponseDto> qnas;

    @Builder
    public QnasResponseDto(PageResponseDto page, List<QnaResponseDto> qnas) {
        this.page = page;
        this.qnas = qnas;
    }
}
