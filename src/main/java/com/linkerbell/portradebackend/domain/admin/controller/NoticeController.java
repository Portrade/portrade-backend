package com.linkerbell.portradebackend.domain.admin.controller;

import com.linkerbell.portradebackend.domain.admin.dto.NoticeDetailResponseDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeListResponseDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeRequestDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeResponseDto;
import com.linkerbell.portradebackend.domain.admin.service.NoticeService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<NoticeResponseDto> writeNoticeApi(
            @RequestBody @Valid NoticeRequestDto noticeRequestDto,
            @AuthenticationPrincipal User user) {
        NoticeResponseDto userResponseDto = noticeService.createNotice(noticeRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @GetMapping
    public ResponseEntity<NoticeListResponseDto> getNoticeListApi(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        NoticeListResponseDto noticeListResponseDto = noticeService.getNoticeList(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(noticeListResponseDto);
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponseDto> getNoticeDetailApi(@PathVariable Long noticeId) {
        NoticeDetailResponseDto noticeDetailResponseDto = noticeService.getNotice(noticeId);
        return ResponseEntity.status(HttpStatus.OK).body(noticeDetailResponseDto);
    }
}
