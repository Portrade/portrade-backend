package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.FollowingsResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.FollowersResponseDto;
import com.linkerbell.portradebackend.domain.user.service.FollowService;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 팔로우 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "회원 팔로우/취소")
    @PatchMapping("/{userId}/follow/{followingId}")
    public ResponseEntity<Void> followApi(
            @Parameter(description = "유저 ID") @PathVariable("userId") String userId,
            @Parameter(description = "팔로잉할 유저 ID") @PathVariable("followingId") String followingId,
            @Parameter(hidden = true) @CurrentUser User user){
        followService.followUser(userId, followingId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "팔로워 목록 조회")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<FollowersResponseDto> getFollowersApi(
            @Parameter(description = "조회할 사용자 ID") @PathVariable("userId") String userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size){
        FollowersResponseDto followersResponseDto = followService.getFollowers(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(followersResponseDto);
    }

    @Operation(summary = "팔로잉 목록 조회")
    @GetMapping("/{userId}/followings")
    public ResponseEntity<FollowingsResponseDto> getFollowingsApi(
            @Parameter(description = "조회할 사용자 ID") @PathVariable("userId") String userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size){
        FollowingsResponseDto followingsResponseDto = followService.getFollowings(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(followingsResponseDto);
    }
}
