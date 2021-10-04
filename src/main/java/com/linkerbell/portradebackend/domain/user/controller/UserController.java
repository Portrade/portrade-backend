package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.ProfileImageResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.UserResponseDto;
import com.linkerbell.portradebackend.domain.user.service.UserService;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
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
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUserApi(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        UserResponseDto userResponseDto = userService.createUser(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PutMapping("/profile/image")
    public ResponseEntity<ProfileImageResponseDto> uploadProfileImageApi(MultipartFile file,
                                                                         @CurrentUser User user) throws IOException {
        ProfileImageResponseDto profileImageResponseDto = userService.uploadProfileImage(user, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(profileImageResponseDto);
    }
}
