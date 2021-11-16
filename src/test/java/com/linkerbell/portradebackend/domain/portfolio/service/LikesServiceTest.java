package com.linkerbell.portradebackend.domain.portfolio.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Likes;
import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.repository.LikesRepository;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @InjectMocks
    private LikesService likesService;
    @Mock
    private LikesRepository likesRepository;
    @Mock
    private PortfolioRepository portfolioRepository;

    private User user;
    private Portfolio portfolio;
    private Likes like;

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
        like = Likes.builder()
                .portfolio(portfolio)
                .user(user)
                .build();
    }

    @DisplayName("포트폴리오 좋아요 성공")
    @Test
    void likePortfolio() throws Exception {
        // given
        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(portfolio));
        given(likesRepository.findByPortfolio_IdAndUser_Username(anyLong(), anyString()))
                .willReturn(Optional.empty());

        // when
        boolean result = likesService.likePortfolio(1L, user);

        // then
        assertTrue(result);
        verify(likesRepository, times(1)).findByPortfolio_IdAndUser_Username(anyLong(), anyString());
        verify(likesRepository, times(1)).save(any(Likes.class));
        verify(likesRepository, times(0)).delete(any(Likes.class));
    }

    @DisplayName("포트폴리오 좋아요 취소 성공")
    @Test
    void cancelLikePortfolio() throws Exception {
        // given
        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(portfolio));
        given(likesRepository.findByPortfolio_IdAndUser_Username(anyLong(), anyString()))
                .willReturn(Optional.ofNullable(like));

        // when
        boolean result = likesService.likePortfolio(1L, user);

        // then
        assertFalse(result);
        verify(likesRepository, times(1)).findByPortfolio_IdAndUser_Username(anyLong(), anyString());
        verify(likesRepository, times(0)).save(any(Likes.class));
        verify(likesRepository, times(1)).delete(any(Likes.class));
    }

    @DisplayName("포트폴리오 좋아요 실패 - 존재하지 않는 포트폴리오 ID")
    @Test
    void likePortfolio_nonexistentPortfolioId() throws Exception {
        // given
        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThrows(NonExistentException.class,
                () -> likesService.likePortfolio(1L, user));
    }
}