package com.linkerbell.portradebackend.domain.user.controller;


import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.SignUpDto;
import com.linkerbell.portradebackend.domain.user.dto.UserDto;
import com.linkerbell.portradebackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> signUpApi(@RequestBody SignUpDto signUpDto) {
        User user = userService.createUser(signUpDto);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .roles(user.getRoles())
                .college(user.getCollege())
                .jobStatus(user.getJobStatus())
                .birthDate(user.getBirthDate())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
