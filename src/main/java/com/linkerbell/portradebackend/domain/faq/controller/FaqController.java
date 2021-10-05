package com.linkerbell.portradebackend.domain.faq.controller;

import com.linkerbell.portradebackend.domain.faq.dto.CreateFaqRequestDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqDetailResponseDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqsResponseDto;
import com.linkerbell.portradebackend.domain.faq.service.FaqService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "자주 묻는 질문 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faqs")
public class FaqController {

    private final FaqService faqService;

    @Operation(summary = "자주 묻는 질문 등록", description = "자주 묻는 질문을 등록한다.")
    @PostMapping
    public ResponseEntity<CreateResponseDto> createFaqApi(
            @RequestBody @Valid CreateFaqRequestDto createFaqRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        CreateResponseDto createdFaq = faqService.createFaq(createFaqRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaq);
    }

    @Operation(summary = "자주 묻는 질문 목록 조회", description = "자주 묻는 질문 목록을 조회한다.")
    @GetMapping
    public ResponseEntity<FaqsResponseDto> getFaqs(
            @Parameter(description="페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description="반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size) {

        FaqsResponseDto faqsResponseDto = faqService.getFaqs(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(faqsResponseDto);
    }

    @Operation(summary = "자주 묻는 질문 상세 조회", description = "자주 묻는 질문을 상세 조회한다.")
    @GetMapping("/{faqId}")
    public ResponseEntity<FaqDetailResponseDto> getFaq(
            @Parameter(description="조회 할 게시글 id") @PathVariable("faqId") Long faqId) {
        FaqDetailResponseDto faqDetailResponseDto = faqService.getFaq(faqId);
        return ResponseEntity.status(HttpStatus.OK).body(faqDetailResponseDto);
    }

    @Operation(summary = "자주 묻는 질문 삭제", description = "자주 묻는 질문을 삭제한다.")
    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFaq(
            @Parameter(description="삭제 할 게시글 id") @PathVariable("faqId") Long faqId) {
        faqService.deleteFaq(faqId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
