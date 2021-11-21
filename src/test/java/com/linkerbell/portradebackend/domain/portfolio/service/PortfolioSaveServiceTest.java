package com.linkerbell.portradebackend.domain.portfolio.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.domain.PortfolioSave;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioSaveRepository;
import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.File;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PortfolioSaveServiceTest {

    @InjectMocks
    private PortfolioSaveService portfolioSaveService;
    @Mock
    private PortfolioSaveRepository portfolioSaveRepository;
    @Mock
    private PortfolioRepository portfolioRepository;

    private User user;
    private Portfolio portfolio;
    private PortfolioSave portfolioSave;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("user")
                .password("1234Aa@@")
                .name("회원1")
                .birthDate("19900903")
                .wantedJob("programmer")
                .profile(Profile.builder()
                        .job("취업준비중")
                        .profileImageFile(File.builder()
                                .fileName("profileImage.png")
                                .extension("png")
                                .url("profile.com")
                                .build())
                        .build())
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
        portfolioSave = PortfolioSave.builder()
                .portfolio(portfolio)
                .user(user)
                .build();
    }

    @DisplayName("포트폴리오 저장 성공")
    @Test
    void savePortfolio() throws Exception {
        // given
        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(portfolio));
        given(portfolioSaveRepository.findByPortfolio_IdAndUser_Username(anyLong(), anyString()))
                .willReturn(Optional.empty());

        // when
        boolean result = portfolioSaveService.savePortfolio(1L, user);

        // then
        assertTrue(result);
        verify(portfolioSaveRepository, times(1)).findByPortfolio_IdAndUser_Username(anyLong(), anyString());
        verify(portfolioSaveRepository, times(1)).save(any(PortfolioSave.class));
        verify(portfolioSaveRepository, times(0)).delete(any(PortfolioSave.class));
    }

    @DisplayName("포트폴리오 저장 취소 성공")
    @Test
    void cancelSavePortfolio() throws Exception {
        // given
        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(portfolio));
        given(portfolioSaveRepository.findByPortfolio_IdAndUser_Username(anyLong(), anyString()))
                .willReturn(Optional.ofNullable(portfolioSave));

        // when
        boolean result = portfolioSaveService.savePortfolio(1L, user);

        // then
        assertFalse(result);
        verify(portfolioSaveRepository, times(1)).findByPortfolio_IdAndUser_Username(anyLong(), anyString());
        verify(portfolioSaveRepository, times(0)).save(any(PortfolioSave.class));
        verify(portfolioSaveRepository, times(1)).delete(any(PortfolioSave.class));
    }

    @DisplayName("포트폴리오 저장 실패 - 존재하지 않는 포트폴리오 ID")
    @Test
    void savePortfolio_nonexistentPortfolioId() throws Exception {
        // given
        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThrows(NonExistentException.class,
                () -> portfolioSaveService.savePortfolio(1L, user));
    }

    @DisplayName("저장된 포트폴리오 목록 조회 성공")
    @Test
    void getSavedPortfolios() throws Exception {
        // given
        Portfolio portfolio1 = Portfolio.builder()
                .id(1L)
                .creator(user)
                .title("포트폴리오 제목1")
                .description("포트폴리오 설명1")
                .category("카테고리")
                .isPublic(true)
                .viewCount(9)
                .contentFiles(Collections.emptyList())
                .likes(Collections.emptyList())
                .comments(Collections.emptyList())
                .mainImageFile(File.builder()
                        .fileName("mainImageFile")
                        .extension("png")
                        .url("main_url").build())
                .build();
        Portfolio portfolio2 = Portfolio.builder()
                .id(2L)
                .creator(user)
                .title("포트폴리오 제목2")
                .description("포트폴리오 설명2")
                .category("카테고리")
                .isPublic(true)
                .viewCount(3)
                .contentFiles(Collections.emptyList())
                .likes(Collections.emptyList())
                .comments(Collections.emptyList())
                .mainImageFile(File.builder()
                        .fileName("mainImageFile")
                        .extension("png")
                        .url("main_url").build())
                .build();
        Portfolio portfolio3 = Portfolio.builder()
                .id(3L)
                .creator(user)
                .title("포트폴리오 제목3")
                .description("포트폴리오 설명3")
                .category("카테고리")
                .isPublic(false)
                .viewCount(15)
                .contentFiles(Collections.emptyList())
                .likes(Collections.emptyList())
                .comments(Collections.emptyList())
                .mainImageFile(File.builder()
                        .fileName("mainImageFile")
                        .extension("png")
                        .url("main_url").build())
                .build();
        PortfolioSave portfolioSave1 = PortfolioSave.builder()
                .portfolio(portfolio1)
                .user(user)
                .build();
        PortfolioSave portfolioSave2 = PortfolioSave.builder()
                .portfolio(portfolio2)
                .user(user)
                .build();
        PortfolioSave portfolioSave3 = PortfolioSave.builder()
                .portfolio(portfolio3)
                .user(user)
                .build();
        List<PortfolioSave> portfolios = new ArrayList<>(Arrays.asList(portfolioSave1, portfolioSave2, portfolioSave3));
        Page<PortfolioSave> portfolioPage = new PageImpl<>(portfolios);

        given(portfolioSaveRepository.findAllByUser(any(Pageable.class), any(User.class)))
                .willReturn(portfolioPage);

        // when
        PortfoliosResponseDto foundSavedPortfoliosResponseDto = portfolioSaveService.getSavedPortfolios(1, 3, user);

        // then
        assertEquals(foundSavedPortfoliosResponseDto.getPage().getTotalPage(), 1);
        assertEquals(foundSavedPortfoliosResponseDto.getPortfolios().size(), 3);
    }
}