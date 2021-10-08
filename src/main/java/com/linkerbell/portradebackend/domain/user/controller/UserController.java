package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.user.dto.*;
import com.linkerbell.portradebackend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<SignUpResponseDto> createUserApi(
            @RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = userService.createUser(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponseDto);
    }

    @Operation(summary = "아이디 중복 확인")
    @GetMapping("/{userId}/exist")
    public ResponseEntity<ExistIdResponseDto> checkUsernameExistsApi(
            @Parameter(description = "사용자 id") @PathVariable("userId") String userId) {
        ExistIdResponseDto existIdResponseDto = userService.checkUsernameExists(userId);
        return ResponseEntity.status(HttpStatus.OK).body(existIdResponseDto);
    }

}
