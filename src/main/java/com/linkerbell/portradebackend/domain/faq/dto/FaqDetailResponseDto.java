package com.linkerbell.portradebackend.domain.faq.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FaqDetailResponseDto {
    private final String content;

    @Builder
    public FaqDetailResponseDto(String content) {
        this.content = content;
    }
}
