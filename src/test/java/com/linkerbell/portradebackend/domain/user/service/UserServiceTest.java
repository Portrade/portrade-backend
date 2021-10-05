package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.ProfileImageResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.SignUpResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import com.linkerbell.portradebackend.global.exception.custom.FileUploadException;
import com.linkerbell.portradebackend.global.exception.custom.InvalidValueException;
import com.linkerbell.portradebackend.global.util.S3Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import java.io.IOException;
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
    @Mock
    private S3Util s3Util;
    @Mock
    private EntityManager entityManager;

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
        SignUpResponseDto savedSignUpResponseDto = userService.createUser(signUpRequestDto);

        // then
        assertEquals("user1", savedSignUpResponseDto.getUserId());
        assertEquals("name1", savedSignUpResponseDto.getName());
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

    @DisplayName("프로필 이미지 변경 성공")
    @Test
    void uploadProfileImage() throws IOException {
        //given
        MockMultipartFile file = new MockMultipartFile(
                "mainImage",
                "mainImage",
                "image/png",
                "mainImage".getBytes());

        UploadResponseDto uploadResponseDto = UploadResponseDto.builder()
                .originalFileName(file.getOriginalFilename())
                .newFileName("newFilename")
                .url("url")
                .build();

        given(s3Util.upload(file)).willReturn(uploadResponseDto);
        given(entityManager.merge(user)).willReturn(user);

        //when
        ProfileImageResponseDto profileImageResponseDto = userService.uploadProfileImage(user, file);

        //then
        assertEquals(uploadResponseDto.getNewFileName(), profileImageResponseDto.getFileName());
        assertEquals(uploadResponseDto.getUrl(), profileImageResponseDto.getUrl());
    }

    @DisplayName("프로필 이미지 변경 실패 - 파일 업로드")
    @Test
    void uploadProfileImage_failure() {
        //given
        MockMultipartFile file = new MockMultipartFile(
                "mainImage",
                "mainImage",
                "image/png",
                "mainImage".getBytes());

        given(s3Util.upload(file)).willThrow(FileUploadException.class);

        //when
        //then
        assertThrows(FileUploadException.class,
                () -> userService.uploadProfileImage(user, file));
    }
}