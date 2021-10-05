package com.linkerbell.portradebackend.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class NoticesResponseDto {

    private final int maxPage;
    private final List<NoticeResponseDto> notices;

    @Builder
    public NoticesResponseDto(int maxPage, List<NoticeResponseDto> notices) {
        this.maxPage = maxPage;
        this.notices = notices;
    }
}
