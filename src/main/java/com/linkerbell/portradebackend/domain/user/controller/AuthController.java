package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.LogInRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.TokenResponseDto;
import com.linkerbell.portradebackend.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> logInApi(@RequestBody @Valid LogInRequestDto logInRequestDto) {
        TokenResponseDto tokenResponseDto = authService.logIn(logInRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logOutApi(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
