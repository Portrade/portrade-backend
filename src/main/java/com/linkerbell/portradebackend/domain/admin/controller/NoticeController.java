package com.linkerbell.portradebackend.domain.admin.controller;

import com.linkerbell.portradebackend.domain.admin.dto.NoticeDetailResponseDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeRequestDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticesResponseDto;
import com.linkerbell.portradebackend.domain.admin.service.NoticeService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
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
    public ResponseEntity<CreateResponseDto> writeNoticeApi
            (@RequestBody @Valid NoticeRequestDto noticeRequestDto,
             @AuthenticationPrincipal User user) {
        CreateResponseDto createResponseDto = noticeService.createNotice(noticeRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponseDto);
    }

    @GetMapping
    public ResponseEntity<NoticesResponseDto> getNoticesApi(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        NoticesResponseDto noticesResponseDto = noticeService.getNotices(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(noticesResponseDto);
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponseDto> getNoticeDetailApi(@PathVariable Long noticeId) {
        NoticeDetailResponseDto noticeDetailResponseDto = noticeService.getNotice(noticeId);
        return ResponseEntity.status(HttpStatus.OK).body(noticeDetailResponseDto);
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<Void> editNoticeApi(@PathVariable Long noticeId,
                                              @RequestBody @Valid NoticeRequestDto noticeRequestDto) {
        noticeService.updateNotice(noticeId, noticeRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNoticeApi(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
