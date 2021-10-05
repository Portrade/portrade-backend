package com.linkerbell.portradebackend.domain.faq.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FaqsResponseDto {

    private final List<FaqResponseDto> faqs;
    private final int maxPage;

    @Builder
    public FaqsResponseDto(List<FaqResponseDto> faqs, int maxPage) {
        this.faqs = faqs;
        this.maxPage = maxPage;
    }
}
