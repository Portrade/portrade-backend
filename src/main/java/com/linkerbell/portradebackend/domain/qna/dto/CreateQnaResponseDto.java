package com.linkerbell.portradebackend.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateQnaResponseDto {
    private final Long id;

    @Builder
    public CreateQnaResponseDto(Long id) {
        this.id = id;
    }
}
