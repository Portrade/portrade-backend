package com.linkerbell.portradebackend.domain.qna.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyQnaRequestDto {
    @NotNull(message = "NULL_QNA_TITLE")
    @Size(min = 1, max = 20, message = "INVALID_QNA_TITLE")
    private String title;

    @NotNull(message = "NULL_QNA_CONTENT")
    @Size(min=1, max = 500, message = "INVALID_QNA_CONTENT")
    private String content;

    @NotNull(message = "NULL_QNA_ISPUBLIC")
    private boolean secret;

    @Builder
    public ReplyQnaRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
