package com.linkerbell.portradebackend.domain.comment.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentsResponseDto {

    private final PageResponseDto page;
    private final List<CommentResponseDto> comments;

    @Builder
    public CommentsResponseDto(PageResponseDto page, List<CommentResponseDto> comments) {
        this.page = page;
        this.comments = comments;
    }
}
