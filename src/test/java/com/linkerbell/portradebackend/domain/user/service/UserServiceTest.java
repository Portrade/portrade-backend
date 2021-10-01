package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.UserResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.exception.custom.InvalidValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("user1")
                .password("1234Aa@@")
                .name("name1")
                .birthDate("12341010")
                .wantedJob("marketing")
                .profile(Profile.builder()
                        .college("college1")
                        .isGraduated(false)
                        .build())
                .build();
    }

    @DisplayName("사용자 생성 성공")
    @Test
    void createUser() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId("user1")
                .name("name1")
                .password("1234Aa@@")
                .wantedJob("marketing")
                .birthDate("12341010")
                .college("college1")
                .graduation(false)
                .build();

        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());
        given(userRepository.save(any(User.class)))
                .willReturn(user);
        given(passwordEncoder.encode(anyString()))
                .willReturn("1234Aa@@");

        // when
        UserResponseDto savedUserResponseDto = userService.createUser(signUpRequestDto);

        // then
        assertEquals("user1", savedUserResponseDto.getUserId());
        assertEquals("name1", savedUserResponseDto.getName());
    }

    @DisplayName("사용자 생성 실패 - 중복된 username")
    @Test
    void createUser_duplicatedUsername() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId("user1")
                .name("name1")
                .password("1234Aa@@")
                .wantedJob("marketing")
                .birthDate("12341010")
                .college("college1")
                .graduation(false)
                .build();

        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.ofNullable(user));

        // when
        // then
        assertThrows(InvalidValueException.class,
                () -> userService.createUser(signUpRequestDto));
    }
}