package com.linkerbell.portradebackend.domain.comment.dto;

import com.linkerbell.portradebackend.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String creator;
    private final String profileImageUrl;
    private final String content;
    private final LocalDateTime createdDate;

    @Builder
    private CommentResponseDto(Long id, String creator, String profileImageUrl, String content, LocalDateTime createdDate) {
        this.id = id;
        this.creator = creator;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.createdDate = createdDate;
    }

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .creator(comment.getCreator().getUsername())
                .profileImageUrl(comment.getCreator().getUserProfileUrl())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .build();
    }
}
