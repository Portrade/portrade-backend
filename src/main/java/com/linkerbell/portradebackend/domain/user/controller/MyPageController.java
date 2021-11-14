package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.portfolio.dto.PortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.InsightResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.JobRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileResponseDto;
import com.linkerbell.portradebackend.domain.user.service.MyPageService;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Tag(name = "사용자 마이페이지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "프로필 사진 업로드")
    @PutMapping("/me/profile/image")
    public ResponseEntity<ProfileResponseDto> uploadProfileImageApi(
            @RequestBody MultipartFile file,
            @Parameter(hidden = true) @CurrentUser User user) {
        ProfileResponseDto profileImageResponseDto = myPageService.uploadProfileImage(user, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(profileImageResponseDto);
    }

    @Operation(summary = "나의 인사이트 조회")
    @GetMapping("/me/insight")
    public ResponseEntity<InsightResponseDto> getMyInsightApi(
            @Parameter(hidden = true) @CurrentUser User user) {
        InsightResponseDto insightResponseDto = myPageService.getMyInsight(user);
        return ResponseEntity.status(HttpStatus.OK).body(insightResponseDto);
    }

    @Operation(summary = "프로필 편집")
    @PutMapping("/me/profile")
    public ResponseEntity<Void> updateProfileApi(
            @RequestBody @Valid ProfileRequestDto profileRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        myPageService.updateProfile(profileRequestDto, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "구직 상태 편집")
    @PutMapping("/me/profile/job")
    public ResponseEntity<Void> updateProfileJobApi(
            @RequestBody @Valid JobRequestDto jobRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        myPageService.updateProfileJob(jobRequestDto, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "특정 사용자의 포트폴리오 목록 조회")
    @GetMapping("/{userId}/portfolios")
    public ResponseEntity<PortfoliosResponseDto> getUserPortfoliosApi(
            @Parameter(description = "사용자 ID") @PathVariable("userId") String userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "6") int size) {
        PortfoliosResponseDto portfoliosResponseDto = myPageService.getUserPortfolios(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(portfoliosResponseDto);
    }

    @Operation(summary = "특정 사용자의 프로필 조회")
    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponseDto> getUserProfileApi(
            @Parameter(description = "사용자 ID") @PathVariable("userId") String userId) {
        ProfileResponseDto profileResponseDto = myPageService.getUserProfile(userId);
        return ResponseEntity.status(HttpStatus.OK).body(profileResponseDto);
    }
}
