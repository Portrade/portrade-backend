package com.linkerbell.portradebackend.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReplyQnaResponseDto {
    private final Long id;

    @Builder
    public ReplyQnaResponseDto(Long id) {
        this.id = id;
    }
}
