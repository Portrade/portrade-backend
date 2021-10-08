package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.ProfileImageResponseDto;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import com.linkerbell.portradebackend.global.exception.custom.FileUploadException;
import com.linkerbell.portradebackend.global.util.S3Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserMyPageServiceTest {

    @InjectMocks
    private UserMyPageService userMyPageService;

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
        ProfileImageResponseDto profileImageResponseDto = userMyPageService.uploadProfileImage(user, file);

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
                () -> userMyPageService.uploadProfileImage(user, file));
    }
}