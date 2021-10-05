package com.linkerbell.portradebackend.domain.faq.controller;

import com.linkerbell.portradebackend.domain.faq.dto.CreateFaqRequestDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqDetailResponseDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqsResponseDto;
import com.linkerbell.portradebackend.domain.faq.service.FaqService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
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

    @PostMapping
    public ResponseEntity<CreateResponseDto> createFaqApi(
            @RequestBody @Valid CreateFaqRequestDto createFaqRequestDto,
            @CurrentUser User user) {
        CreateResponseDto createdFaq = faqService.createFaq(createFaqRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaq);
    }

    @GetMapping
    public ResponseEntity<FaqsResponseDto> getFaqs(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        FaqsResponseDto faqsResponseDto = faqService.getFaqs(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(faqsResponseDto);
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<FaqDetailResponseDto> getFaq(@PathVariable("faqId") Long faqId) {
        FaqDetailResponseDto faqDetailResponseDto = faqService.getFaq(faqId);
        return ResponseEntity.status(HttpStatus.OK).body(faqDetailResponseDto);
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFaq(@PathVariable("faqId") Long faqId) {
        faqService.deleteFaq(faqId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
