package com.linkerbell.portradebackend.domain.faq.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FaqsResponseDto {

    private final PageResponseDto page;
    private final List<FaqResponseDto> faqs;

    @Builder
    public FaqsResponseDto(PageResponseDto page, List<FaqResponseDto> faqs) {
        this.page = page;
        this.faqs = faqs;
    }
}
