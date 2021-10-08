package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.FollowingsResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.FollowersResponseDto;
import com.linkerbell.portradebackend.domain.user.service.UserFollowService;
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
public class UserFollowController {

    private final UserFollowService userFollowService;

    @Operation(summary = "회원 팔로우/취소")
    @PatchMapping("/{userId}/follow/{following-Id}")
    public ResponseEntity<Void> followApi(
            @Parameter(description = "유저 Id") @PathVariable("userId") String userId,
            @Parameter(description = "팔로잉 할 유저 Id") @PathVariable("following-Id") String followingId,
            @Parameter(hidden = true) @CurrentUser User user){
        userFollowService.followUser(userId, followingId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "팔로워 목록 조회")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<FollowersResponseDto> getFollowsApi(
            @Parameter(description = "조회 할 유저 Id") @PathVariable("userId") String userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size){
        FollowersResponseDto followersResponseDto = userFollowService.getFollowers(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(followersResponseDto);
    }

    @Operation(summary = "팔로잉 목록 조회")
    @GetMapping("/{userId}/followings")
    public ResponseEntity<FollowingsResponseDto> getFollowersApi(
            @Parameter(description = "조회 할 유저 Id") @PathVariable("userId") String userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size){
        FollowingsResponseDto followingsResponseDto = userFollowService.getFollowings(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(followingsResponseDto);
    }
}
