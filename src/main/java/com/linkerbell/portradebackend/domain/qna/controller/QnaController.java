package com.linkerbell.portradebackend.domain.qna.controller;

import com.linkerbell.portradebackend.domain.qna.dto.*;
import com.linkerbell.portradebackend.domain.qna.service.QnaService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qnas")
public class QnaController {

    private final QnaService qnaService;

    //로그인한 유저만 접근 가능
    @PostMapping
    public ResponseEntity<CreateQnaResponseDto> saveQnaApi(
            @RequestBody @Valid CreateQnaRequestDto requestDto,
            @AuthenticationPrincipal User user) {

        CreateQnaResponseDto savedQnaResponseDto = qnaService.createQna(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQnaResponseDto);
    }

    //로그인한 관리자만 접근 가능
    @PostMapping("/{qnaId}/answer")
    public ResponseEntity<ReplyQnaResponseDto> replyQnaApi(
            @PathVariable("qnaId") Long qnaId,
            @RequestBody @Valid ReplyQnaRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        ReplyQnaResponseDto replyQnaResponseDto = qnaService.createReplyQna(qnaId, requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(replyQnaResponseDto);
    }

    @GetMapping
    public ResponseEntity<QnasResponseDto> getQnasApi(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        QnasResponseDto qnasResponseDto = qnaService.getQnas(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(qnasResponseDto);
    }

    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaDetailResponseDto> getQnaDetailApi(
            @PathVariable("qnaId") Long qnaId,
            @AuthenticationPrincipal User user){
        QnaDetailResponseDto qnaDetailResponseDto = qnaService.getQna(qnaId, user);
        return ResponseEntity.status(HttpStatus.OK).body(qnaDetailResponseDto);
    }
}
