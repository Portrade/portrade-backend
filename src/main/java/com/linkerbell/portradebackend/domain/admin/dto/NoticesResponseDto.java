package com.linkerbell.portradebackend.domain.admin.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticesResponseDto {

    private int maxPage;
    private List<NoticeResponseDto> notices;

    @Builder
    public NoticesResponseDto(int maxPage, List<NoticeResponseDto> notices) {
        this.maxPage = maxPage;
        this.notices = notices;
    }
}
