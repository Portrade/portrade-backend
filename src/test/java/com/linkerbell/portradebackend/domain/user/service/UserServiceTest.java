package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.UserResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
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

    static String username = "username1";
    static String password = "password1";
    static String name = "name1";
    static String birthDate = "12341010";
    static String wantedJob = "marketing";
    static String college = "college1";
    static boolean isGraduated = false;

    private User user;

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        user = User.builder()
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
    }

    @DisplayName("User 생성")
    @Test
    void createUser() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId(username)
                .name(name)
                .password(password)
                .wantedJob(wantedJob)
                .birthDate(birthDate)
                .college(college)
                .graduation(isGraduated)
                .build();

        given(userRepository.findByUsername(username))
                .willReturn(Optional.empty());
        given(userRepository.save(any(User.class)))
                .willReturn(user);
        given(passwordEncoder.encode(anyString()))
                .willReturn(password);

        // when
        UserResponseDto savedUserResponseDto = userService.createUser(signUpRequestDto);

        // then
        assertEquals(username, savedUserResponseDto.getUserId());
        assertEquals(name, savedUserResponseDto.getName());
    }

    @DisplayName("User 생성 - 중복된 username")
    @Test
    void createUser_duplicatedUsername() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId(username)
                .name(name)
                .password(password)
                .wantedJob(wantedJob)
                .birthDate(birthDate)
                .college(college)
                .graduation(isGraduated)
                .build();

        given(userRepository.findByUsername(username))
                .willReturn(Optional.ofNullable(user));

        // when
        // then
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(signUpRequestDto));
    }
}