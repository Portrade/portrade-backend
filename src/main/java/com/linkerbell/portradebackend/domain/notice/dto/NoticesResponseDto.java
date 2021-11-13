package com.linkerbell.portradebackend.domain.notice.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class NoticesResponseDto {

    private final PageResponseDto page;
    private final List<NoticeResponseDto> notices;

    @Builder
    public NoticesResponseDto(PageResponseDto page, List<NoticeResponseDto> notices) {
        this.page = page;
        this.notices = notices;
    }
}
