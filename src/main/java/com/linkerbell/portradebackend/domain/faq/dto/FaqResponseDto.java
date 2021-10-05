package com.linkerbell.portradebackend.domain.faq.dto;

import com.linkerbell.portradebackend.domain.faq.domain.Faq;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class FaqResponseDto {
    private final Long id;
    private final String title;
    private final String content;

    @Builder
    public FaqResponseDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static FaqResponseDto of(Faq faq) {
        return FaqResponseDto.builder()
                .id(faq.getId())
                .title(faq.getTitle())
                .content(faq.getContent())
                .build();
    }
}
