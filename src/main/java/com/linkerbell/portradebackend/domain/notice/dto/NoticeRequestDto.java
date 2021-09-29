package com.linkerbell.portradebackend.domain.notice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeRequestDto {

    @NotNull(message = "NULL_NOTICE_TITLE")
    private String title;
    @NotNull(message = "NULL_NOTICE_CONTENT")
    private String content;

    @Builder
    public NoticeRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
