package com.linkerbell.portradebackend.domain.portfolio.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.dto.CreatePortfolioRequestDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfolioDetailResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.File;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.util.S3Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @InjectMocks
    private PortfolioService portfolioService;
    @Mock
    private PortfolioRepository portfolioRepository;
    @Mock
    private S3Util s3Util;

    private User user;
    private Portfolio portfolio;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("user1")
                .password("1234Aa@@")
                .name("회원1")
                .birthDate("20030322")
                .wantedJob("marketing")
                .build();
        portfolio = Portfolio.builder()
                .id(1L)
                .creator(user)
                .title("포트폴리오 제목1")
                .description("포트폴리오 설명1")
                .category("카테고리")
                .isPublic(true)
                .viewCount(10)
                .contentFiles(Collections.emptyList())
                .likes(Collections.emptyList())
                .comments(Collections.emptyList())
                .build();
    }

    @DisplayName("포트폴리오 생성 성공")
    @Test
    void createPortfolio() throws Exception {
        // given
        MockMultipartFile mockMainImageFile = new MockMultipartFile(
                "mainImage",
                "mainImage",
                "image/png",
                "mainImage".getBytes());
        MockMultipartFile mockContentFile = new MockMultipartFile(
                "contentFiles",
                "contentFile",
                "image/png",
                "contentFiles".getBytes());
        CreatePortfolioRequestDto createPortfolioRequestDto = CreatePortfolioRequestDto.builder()
                .title("포트폴리오 제목")
                .description("포트폴리오 설명")
                .category("marketing")
                .isPublic(false)
                .mainImageFile(mockMainImageFile)
                .contentFiles(List.of(mockContentFile))
                .build();
        File mainFile = File.builder()
                .fileName("mainImageFile")
                .extension("png")
                .url("main_url").build();
        File contentFile = File.builder()
                .fileName("contentFile")
                .extension("png")
                .url("content_url").build();

        given(s3Util.upload(any(MultipartFile.class)))
                .willReturn(mainFile)
                .willReturn(contentFile);

        // when
        portfolioService.createPortfolio(createPortfolioRequestDto, user);

        // then
        verify(portfolioRepository, times(1))
                .save(any(Portfolio.class));
    }

    @DisplayName("포트폴리오 상세 조회 성공")
    @Test
    void getPortfolio() throws Exception {
        // given
        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(portfolio));

        // when
        PortfolioDetailResponseDto foundPortfolioDetailResponseDto = portfolioService.getPortfolio(1L, user);

        // then
        assertEquals(portfolio.getId(), foundPortfolioDetailResponseDto.getId());
        assertEquals(portfolio.getTitle(), foundPortfolioDetailResponseDto.getTitle());
        assertEquals(portfolio.getDescription(), foundPortfolioDetailResponseDto.getDescription());
        assertEquals(portfolio.getCategory(), foundPortfolioDetailResponseDto.getCategory());
        assertEquals(portfolio.isPublic(), foundPortfolioDetailResponseDto.getIsPublic());
    }

    @DisplayName("포트폴리오 상세 조회 실패 - 존재하지 않는 포트폴리오 ID")
    @Test
    void getPortfolio_nonexistentId() throws Exception {
        // given
        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        // then
        Assertions.assertThrows(NonExistentException.class,
                () -> portfolioService.getPortfolio(1L, user));
    }

    @DisplayName("포트폴리오 상세 조회 성공 - 조회수 증가 확인")
    @Test
    void getPortfolio_addViewCount() throws Exception {
        // given
        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(portfolio));

        // when
        PortfolioDetailResponseDto foundPortfolioDetailResponseDto = portfolioService.getPortfolio(1L, user);

        // then
        assertEquals(11, foundPortfolioDetailResponseDto.getViewCount());
    }
}