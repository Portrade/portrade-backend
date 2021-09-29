package com.linkerbell.portradebackend.global.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PageResponseDto {
    private final int totalPage;
    private final int totalElement;

    @Builder
    public PageResponseDto(int totalPage, int totalElement) {
        this.totalPage = totalPage;
        this.totalElement = totalElement;
    }
}
