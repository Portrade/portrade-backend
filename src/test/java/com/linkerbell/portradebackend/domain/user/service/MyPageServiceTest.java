package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.comment.domain.Comment;
import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfolioResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.portfolio.domain.Likes;
import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.InsightResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.FollowRepository;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.common.File;
import com.linkerbell.portradebackend.global.exception.custom.FileHandlingException;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.util.S3Util;
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
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

    @InjectMocks
    private MyPageService myPageService;
    @Mock
    private S3Util s3Util;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PortfolioRepository portfolioRepository;
    @Mock
    private FollowRepository followRepository;

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
                        .job("취업준비중")
                        .profileImageFile(File.builder()
                                .fileName("profileImage.png")
                                .extension("png")
                                .url("profile.com")
                                .build())
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
        File uploadedProfileImage = File.builder()
                .fileName("newFilename")
                .url("url")
                .build();

        given(s3Util.upload(file))
                .willReturn(uploadedProfileImage);

        //when
        ProfileResponseDto profileResponseDto = myPageService.uploadProfileImage(user, file);

        //then
        assertEquals(user.getUsername(), profileResponseDto.getId());
        assertEquals(user.getName(), profileResponseDto.getName());
        assertEquals(uploadedProfileImage.getUrl(), profileResponseDto.getProfileImageUrl());

        verify(userRepository, times(1)).save(any(User.class));
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

        given(s3Util.upload(file))
                .willThrow(FileHandlingException.class);

        //when
        //then
        assertThrows(FileHandlingException.class,
                () -> myPageService.uploadProfileImage(user, file));
    }

    @DisplayName("특정 사용자의 포트폴리오 목록 조회 성공")
    @Test
    void getUserPortfolios() {
        //given
        Portfolio portfolio1 = Portfolio.builder()
                .creator(user)
                .title("포트폴리오1")
                .description("저의 첫번째 포트폴리오입니다.")
                .category("security")
                .mainImageFile(File.builder()
                        .url("www.main.com")
                        .build())
                .isPublic(true)
                .build();
        Portfolio portfolio2 = Portfolio.builder()
                .creator(user)
                .title("포트폴리오2")
                .description("저의 두번째 포트폴리오입니다.")
                .category("security")
                .mainImageFile(File.builder()
                        .url("www.main.com")
                        .build())
                .isPublic(true)
                .build();
        Portfolio portfolio3 = Portfolio.builder()
                .creator(user)
                .title("포트폴리오3")
                .description("저의 세번째 포트폴리오입니다.")
                .category("security")
                .mainImageFile(File.builder()
                        .url("www.main.com")
                        .build())
                .isPublic(true)
                .build();
        List<Portfolio> portfolios = new ArrayList<>(List.of(portfolio1, portfolio2, portfolio3));
        Page<Portfolio> portfoliosPage = new PageImpl<>(portfolios);

        given(portfolioRepository.findAllByCreator_Username(any(Pageable.class), anyString()))
                .willReturn(portfoliosPage);

        //when
        PortfoliosResponseDto userPortfolios = myPageService.getUserPortfolios(user.getUsername(), 1, 10);

        //then
        assertEquals(3, userPortfolios.getPortfolios().size());
        assertEquals(1, userPortfolios.getPage().getTotalPage());
        List<PortfolioResponseDto> portfolioResponseDtos = userPortfolios.getPortfolios();
        assertEquals(portfolio1.getTitle(), portfolioResponseDtos.get(0).getTitle());
        assertEquals(portfolio1.getCreatedDate(), portfolioResponseDtos.get(0).getCreatedDate());
        assertEquals(portfolio2.getTitle(), portfolioResponseDtos.get(1).getTitle());
        assertEquals(portfolio2.getCreatedDate(), portfolioResponseDtos.get(1).getCreatedDate());
        assertEquals(portfolio3.getTitle(), portfolioResponseDtos.get(2).getTitle());
        assertEquals(portfolio3.getCreatedDate(), portfolioResponseDtos.get(2).getCreatedDate());
    }

    @DisplayName("프로필 조회 실패 - 존재하지 않는 Username")
    @Test
    void getUserProfile_nonexistentUsername() {
        //given
        given(userRepository.findByUsername(user.getUsername()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(NonExistentException.class, () ->
                myPageService.getUserProfile(user.getUsername()));
    }

    @DisplayName("프로필 조회 성공")
    @Test
    void getUserProfile() {
        //given
        given(userRepository.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        //when
        ProfileResponseDto userProfileDto = myPageService.getUserProfile(user.getUsername());

        //then
        assertEquals(user.getName(), userProfileDto.getName());
        assertEquals(user.getUserProfileUrl(), userProfileDto.getProfileImageUrl());
        assertEquals(user.getUserJob(), userProfileDto.getJob());
    }

    @DisplayName("프로필 수정 성공")
    @Test
    void updateProfile() {
        //given
        ProfileRequestDto profileRequestDto = ProfileRequestDto.builder()
                .name("김수정")
                .birthDate("19990909")
                .wantedJob("programmer")
                .college("수정대학교")
                .isGraduated(true)
                .build();

        //when
        myPageService.updateProfile(profileRequestDto, user);

        //then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("나의 인사이트 조회 성공")
    @Test
    void getMyInsight() {
        //given
        Portfolio portfolio1 = null;
        Portfolio portfolio2 = null;
        Portfolio portfolio3 = null;
        Likes likes1 = Likes.builder()
                .user(user)
                .portfolio(portfolio1)
                .build();
        Likes likes2 = Likes.builder()
                .user(user)
                .portfolio(portfolio2)
                .build();
        Likes likes3 = Likes.builder()
                .user(user)
                .portfolio(portfolio3)
                .build();
        Comment comment = Comment.builder()
                .creator(user)
                .portfolio(portfolio1)
                .content("댓글 남겨요!")
                .build();
        portfolio1 = Portfolio.builder()
                .creator(user)
                .title("포트폴리오1")
                .description("저의 첫번째 포트폴리오입니다.")
                .category("security")
                .isPublic(true)
                .viewCount(100)
                .likes(new ArrayList<>(List.of(likes1)))
                .comments(new ArrayList<>(List.of(comment)))
                .build();
        portfolio2 = Portfolio.builder()
                .creator(user)
                .title("포트폴리오2")
                .description("저의 두번째 포트폴리오입니다.")
                .category("security")
                .isPublic(true)
                .likes(new ArrayList<>(List.of(likes2)))
                .comments(new ArrayList<>())
                .viewCount(50)
                .build();
        portfolio3 = Portfolio.builder()
                .creator(user)
                .title("포트폴리오3")
                .description("저의 세번째 포트폴리오입니다.")
                .category("security")
                .isPublic(true)
                .likes(new ArrayList<>(List.of(likes3)))
                .comments(new ArrayList<>())
                .viewCount(20)
                .build();
        List<Portfolio> portfolios = new ArrayList<>(List.of(portfolio1, portfolio2, portfolio3));

        given(portfolioRepository.findAllByCreator_Username(user.getUsername()))
                .willReturn(portfolios);
        given(followRepository.countByFollowing_Username(any()))
                .willReturn(1);
        given(followRepository.countByFollower_Username(any()))
                .willReturn(4);

        //when
        InsightResponseDto myInsight = myPageService.getMyInsight(user);

        //then
        assertEquals(170, myInsight.getViewCount());
        assertEquals(3, myInsight.getLikeCount());
        assertEquals(1, myInsight.getCommentCount());
        assertEquals(4, myInsight.getFollowerCount());
        assertEquals(1, myInsight.getFollowingCount());
    }
}