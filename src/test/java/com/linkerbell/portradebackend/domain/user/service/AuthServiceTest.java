package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.LogInRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.TokenResponseDto;
import com.linkerbell.portradebackend.global.config.security.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .username("test")
                .password("1111")
                .build();
    }

    @Test
    public void 로그인_아이디없는경우() {
        final LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .userId("testt")
                .password("1111")
                .build();
        //UsernamePasswordAuthenticationToken original = new UsernamePasswordAuthenticationToken(logInRequestDto.getUserId(), logInRequestDto.getPassword());

        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> authService.logIn(logInRequestDto));
        assertThat(result.getMessage()).isEqualTo("아이디 또는 비밀번호가 잘못 입력 되었습니다.");
    }

    @Test
    public void 로그인성공() {
        final LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .userId("test")
                .password("1111")
                .build();

        final TokenResponseDto result = authService.logIn(logInRequestDto);
        //assertThat()
    }
}