package com.linkerbell.portradebackend.domain.qna.controller;

import com.linkerbell.portradebackend.domain.qna.dto.CreateQnaRequestDto;
import com.linkerbell.portradebackend.domain.qna.dto.QnaDetailResponseDto;
import com.linkerbell.portradebackend.domain.qna.dto.QnasResponseDto;
import com.linkerbell.portradebackend.domain.qna.dto.ReplyQnaRequestDto;
import com.linkerbell.portradebackend.domain.qna.service.QnaService;
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

@Tag(name = "1:1 문의 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qnas")
public class QnaController {

    private final QnaService qnaService;

    @Operation(summary = "1:1 문의 게시글 등록", description = "1:1 문의 게시글을 등록한다.")
    @PostMapping
    public ResponseEntity<IdResponseDto> createQuestionApi(
            @RequestBody @Valid CreateQnaRequestDto requestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        IdResponseDto idResponseDto = qnaService.createQuestion(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(idResponseDto);
    }

    @Operation(summary = "1:1 문의 답변 등록", description = "1:1 문의 게시글의 답변을 등록한다.")
    @PostMapping("/{qnaId}/answer")
    public ResponseEntity<IdResponseDto> createAnswerApi(
            @Parameter(description = "답변 할 게시글 ID") @PathVariable("qnaId") Long qnaId,
            @RequestBody @Valid ReplyQnaRequestDto requestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        IdResponseDto idResponseDto = qnaService.createAnswer(qnaId, requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(idResponseDto);
    }

    @Operation(summary = "1:1 문의 게시글 목록 조회", description = "1:1 문의 게시글 목록을 조회한다.")
    @GetMapping
    public ResponseEntity<QnasResponseDto> getQnasApi(
            @Parameter(description = "검색할 키워드") @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size) {
        QnasResponseDto qnasResponseDto = qnaService.getQnas(page, size, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(qnasResponseDto);
    }

    @Operation(summary = "1:1 문의 게시글 상세 조회", description = "1:1 문의 게시글을 상세 조회한다.")
    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaDetailResponseDto> getQnaDetailApi(
            @Parameter(description = "조회 할 게시글 ID") @PathVariable("qnaId") Long qnaId,
            @Parameter(hidden = true) @CurrentUser User user) {
        QnaDetailResponseDto qnaDetailResponseDto = qnaService.getQna(qnaId, user);
        return ResponseEntity.status(HttpStatus.OK).body(qnaDetailResponseDto);
    }

    @Operation(summary = "1:1 문의 게시글 삭제", description = "1:1 문의 게시글을 삭제한다.")
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<Void> deleteQnaApi(@PathVariable("qnaId") Long qnaId, @CurrentUser User user) {
        qnaService.deleteQna(qnaId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
