package com.linkerbell.portradebackend.domain.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequestDto {

    @NotNull(message = "NULL_COMMENT_CONTENT")
    private String content;

    @Builder
    public CommentRequestDto(String content) {
        this.content = content;
    }
}
