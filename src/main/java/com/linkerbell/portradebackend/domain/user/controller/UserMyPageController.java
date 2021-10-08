package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.*;
import com.linkerbell.portradebackend.domain.user.service.UserMyPageService;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserMyPageController {

    private final UserMyPageService userMyPageService;

    //user 권한 필요
    @Operation(summary = "프로필 사진 업로드")
    @PutMapping("/me/profile/image")
    public ResponseEntity<ProfileImageResponseDto> uploadProfileImageApi(
            MultipartFile file,
            @Parameter(hidden = true) @CurrentUser User user) throws IOException {
        ProfileImageResponseDto profileImageResponseDto = userMyPageService.uploadProfileImage(user, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(profileImageResponseDto);
    }

    @Operation(summary = "나의 인사이트")
    @GetMapping("/me/insight")
    public ResponseEntity<InsightResponseDto> getMyInsightApi(
            @Parameter(hidden = true) @CurrentUser User user) {
        InsightResponseDto insightResponseDto = userMyPageService.getMyInsight(user);
        return ResponseEntity.status(HttpStatus.OK).body(insightResponseDto);
    }

    @Operation(summary = "프로필 편집")
    @PutMapping("/me/profile")
    public ResponseEntity<Void> updateProfileApi(
            @RequestBody @Valid ProfileRequestDto profileRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        userMyPageService.updateProfile(profileRequestDto, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "구직 상태 편집")
    @PutMapping("/me/profile/job")
    public ResponseEntity<Void> updateProfileJobApi(
            @RequestBody @Valid JobRequestDto jobRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        userMyPageService.updateProfileJob(jobRequestDto, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "특정 사용자의 포트폴리오 목록 조회")
    @GetMapping("/{userId}/portfolios")
    public ResponseEntity<UserPortfoliosResponseDto> getUserPortfoliosApi(
            @PathVariable("userId") String userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size){
        UserPortfoliosResponseDto portfolioResponseDto = userMyPageService.getUserPortfolios(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(portfolioResponseDto);
    }

    @Operation(summary = "특정 사용자의 프로필 조회")
    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponeDto> getUserProfile(
            @PathVariable("userId") String userId) {
        ProfileResponeDto profileResponeDto = userMyPageService.getUserProfile(userId);
        return ResponseEntity.status(HttpStatus.OK).body(profileResponeDto);
    }
}
