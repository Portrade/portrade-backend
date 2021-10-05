package com.linkerbell.portradebackend.domain.qna.controller;

import com.linkerbell.portradebackend.domain.qna.dto.*;
import com.linkerbell.portradebackend.domain.qna.service.QnaService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qnas")
public class QnaController {

    private final QnaService qnaService;

    //로그인한 유저만 접근 가능
    @PostMapping
    public ResponseEntity<CreateResponseDto> createQuestionApi(
            @RequestBody @Valid CreateQnaRequestDto requestDto,
            @CurrentUser User user) {

        CreateResponseDto createQnaResponseDto = qnaService.createQuestion(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createQnaResponseDto);
    }

    //로그인한 관리자만 접근 가능
    @PostMapping("/{qnaId}/answer")
    public ResponseEntity<CreateResponseDto> createAnswerApi(
            @PathVariable("qnaId") Long qnaId,
            @RequestBody @Valid ReplyQnaRequestDto requestDto,
            @CurrentUser User user) {
        CreateResponseDto createQnaResponseDto = qnaService.createAnswer(qnaId, requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createQnaResponseDto);
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
            @CurrentUser User user){
        QnaDetailResponseDto qnaDetailResponseDto = qnaService.getQna(qnaId, user);
        return ResponseEntity.status(HttpStatus.OK).body(qnaDetailResponseDto);
    }

    @DeleteMapping("/{qnaId}")
    public ResponseEntity<Void> deleteQnaApi(@PathVariable("qnaId") Long qnaId, @CurrentUser User user) {
        qnaService.deleteQna(qnaId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
