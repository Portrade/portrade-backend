package com.linkerbell.portradebackend.domain.comment.controller;

import com.linkerbell.portradebackend.domain.comment.dto.CommentRequestDto;
import com.linkerbell.portradebackend.domain.comment.dto.CommentsResponseDto;
import com.linkerbell.portradebackend.domain.comment.service.CommentService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "포트폴리오 댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 등록", description = "특정 포트폴리오에 댓글을 등록한다.")
    @PostMapping("/{portfolioId}")
    public ResponseEntity<IdResponseDto> createCommentApi(
            @Parameter(description = "댓글 등록할 포트폴리오 ID") @PathVariable("portfolioId") Long portfolioId,
            @RequestBody @Valid CommentRequestDto commentRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        IdResponseDto idResponseDto = commentService.createComment(commentRequestDto, portfolioId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(idResponseDto);
    }

    @Operation(summary = "댓글 목록 조회", description = "특정 포트폴리오의 댓글 목록을 조회한다.")
    @GetMapping("/{portfolioId}")
    public ResponseEntity<CommentsResponseDto> getCommentsApi(
            @Parameter(description = "댓글 목록을 조회할 포트폴리오 ID") @PathVariable("portfolioId") Long portfolioId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size) {
        CommentsResponseDto commentsResponseDto = commentService.getComments(portfolioId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(commentsResponseDto);
    }

    @Operation(summary = "댓글 삭제", description = "특정 포트폴리오의 댓글을 삭제한다.")
    @DeleteMapping("/{portfolioId}/{commentId}")
    public ResponseEntity<Void> deleteCommentApi(
            @Parameter(description = "댓글을 삭제할 포트폴리오 ID") @PathVariable("portfolioId") Long portfolioId,
            @Parameter(description = "삭제할 댓글 ID") @PathVariable("commentId") Long commentId,
            @Parameter(hidden = true) @CurrentUser User user) {
        commentService.deleteComment(portfolioId, commentId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
