package com.linkerbell.portradebackend.domain.notice.controller;

import com.linkerbell.portradebackend.domain.notice.dto.NoticeDetailResponseDto;
import com.linkerbell.portradebackend.domain.notice.dto.NoticeRequestDto;
import com.linkerbell.portradebackend.domain.notice.dto.NoticesResponseDto;
import com.linkerbell.portradebackend.domain.notice.service.NoticeService;
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

@Tag(name = "공지사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 등록", description = "공지사항을 작성한다.")
    @PostMapping
    public ResponseEntity<IdResponseDto> createNoticeApi(
            @RequestBody @Valid NoticeRequestDto noticeRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        IdResponseDto idResponseDto = noticeService.createNotice(noticeRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(idResponseDto);
    }

    @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 조회한다.")
    @GetMapping
    public ResponseEntity<NoticesResponseDto> getNoticesApi(
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "검색할 키워드") @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        NoticesResponseDto noticesResponseDto = noticeService.getNotices(page, size, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(noticesResponseDto);
    }

    @Operation(summary = "공지사항 상세 조회", description = "공지사항을 상세 조회한다.")
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailResponseDto> getNoticeDetailApi(
            @Parameter(description = "상세 조회할 공지사항 ID") @PathVariable Long noticeId) {
        NoticeDetailResponseDto noticeDetailResponseDto = noticeService.getNotice(noticeId);
        return ResponseEntity.status(HttpStatus.OK).body(noticeDetailResponseDto);
    }

    @Operation(summary = "공지사항 수정", description = "공지사항을 수정한다.")
    @PutMapping("/{noticeId}")
    public ResponseEntity<IdResponseDto> editNoticeApi(
            @Parameter(description = "수정할 공지사항 ID") @PathVariable Long noticeId,
            @RequestBody @Valid NoticeRequestDto noticeRequestDto) {
        IdResponseDto idResponseDto = noticeService.updateNotice(noticeId, noticeRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(idResponseDto);
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제한다.")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNoticeApi(
            @Parameter(description = "삭제할 공지사항 ID") @PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
