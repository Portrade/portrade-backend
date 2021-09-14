package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.LogInDto;
import com.linkerbell.portradebackend.domain.user.dto.TokenDto;
import com.linkerbell.portradebackend.domain.user.dto.UserDto;
import com.linkerbell.portradebackend.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> logInApi(@RequestBody LogInDto logInDto) {
        TokenDto tokenDto = authService.logIn(logInDto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logOutApi(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Security test
    @GetMapping("/user")
    public ResponseEntity<UserDto> checkUserRoleApi(@AuthenticationPrincipal User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .roles(user.getRoles())
                .college(user.getCollege())
                .jobStatus(user.getJobStatus())
                .birthDate(user.getBirthDate())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    // Security test
    @GetMapping("/admin")
    public ResponseEntity<UserDto> checkAdminRoleApi(@AuthenticationPrincipal User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .roles(user.getRoles())
                .college(user.getCollege())
                .jobStatus(user.getJobStatus())
                .birthDate(user.getBirthDate())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
}
