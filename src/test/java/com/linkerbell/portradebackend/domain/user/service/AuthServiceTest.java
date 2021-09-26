package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.LogInRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.TokenResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import com.linkerbell.portradebackend.global.config.security.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    static String username = "test";
    static String password = "1111";
    static String name = "name1";
    static String birthDate = "12341010";
    static String wantedJob = "marketing";
    static String college = "college1";
    static boolean isGraduated = false;

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    User user;
    UserAdapter userAdapter;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password(password)
                .name(name)
                .birthDate(birthDate)
                .wantedJob(wantedJob)
                .profile(Profile.builder()
                        .college(college)
                        .isGraduated(isGraduated)
                        .build())
                .build();

        log.info("userPassword= {}", user.getPassword());
        User savedUser = userRepository.save(user);
        log.info("savedUserPassword= {}", savedUser.getPassword());
        userAdapter = new UserAdapter(user);
        log.info("{}", userAdapter.getPassword());
    }

    @Test
    public void 로그인_아이디없는경우() {
        final LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .userId("testt")
                .password("1111")
                .build();

        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> authService.logIn(logInRequestDto));
        assertThat(result.getMessage()).isEqualTo("아이디 또는 비밀번호가 잘못 입력 되었습니다.");
    }

    @Test
    @WithAnonymousUser
    public void 로그인성공() {

        final LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .userId("test")
                .password("1111")
                .build();

        final TokenResponseDto result = authService.logIn(logInRequestDto);
        assertThat(result.getAccessToken()).isNotNull();
    }
}