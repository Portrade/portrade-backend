package com.linkerbell.portradebackend.domain.comment.dto;

import com.linkerbell.portradebackend.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String creator;
    private final String content;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    public CommentResponseDto(Long id, String creator, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.creator = creator;
        this.content = content;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .creator(comment.getCreator().getUsername())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .lastModifiedDate(comment.getLastModifiedDate())
                .build();
    }
}
