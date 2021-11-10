package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Follow;
import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.FollowersResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.FollowingsResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.FollowRepository;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
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
class FollowServiceTest {

    @InjectMocks
    private FollowService followService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FollowRepository followRepository;

    private User follower;
    private User following;

    @BeforeEach
    void setUp() {
        follower = User.builder()
                .username("follower1")
                .password("1234Aa@@")
                .name("팔로워1")
                .birthDate("19900903")
                .wantedJob("programmer")
                .profile(Profile
                        .builder()
                        .job("취업준비중")
                        .profileUrl("https://1")
                        .build())
                .build();
        following = User.builder()
                .username("users1")
                .password("1234Aa@@")
                .name("팔로잉1")
                .birthDate("19900903")
                .wantedJob("programmer")
                .profile(Profile
                        .builder()
                        .job("취업준비중")
                        .profileUrl("https://2")
                        .build())
                .build();
    }

    @DisplayName("회원 팔로우 실패 - 존재하지 않는 유저")
    @Test
    void followUser_nonexistentId() {
        //given
        given(userRepository.findByUsername(following.getUsername()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(NonExistentException.class,
                () -> followService.followUser(follower.getUsername(), following.getUsername(), follower));
    }

    @DisplayName("회원 팔로우/취소 실패 - 권한 없음")
    @Test
    void followUser_unauthorizedUser() {
        //given
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.of(following));

        //when
        //then
        assertThrows(UnAuthorizedException.class,
                () -> followService.followUser(follower.getUsername(), following.getUsername(), following));
    }

    @DisplayName("회원 팔로우 성공")
    @Test
    void followUser_follow() {
        //given
        given(userRepository.findByUsername(following.getUsername()))
                .willReturn(Optional.of(following));
        given(userRepository.findByUsername(follower.getUsername()))
                .willReturn(Optional.of(follower));
        given(followRepository.findByFollowerIdAndFollowingId(follower.getUsername(), following.getUsername()))
                .willReturn(Optional.empty());

        //when
        followService.followUser(follower.getUsername(), following.getUsername(), follower);

        //then
        verify(followRepository, times(1)).findByFollowerIdAndFollowingId(anyString(), anyString());
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @DisplayName("회원 팔로우 취소 성공")
    @Test
    void followUser_cancel() {
        //given
        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        given(userRepository.findByUsername(following.getUsername()))
                .willReturn(Optional.of(following));
        given(userRepository.findByUsername(follower.getUsername()))
                .willReturn(Optional.of(follower));
        given(followRepository.findByFollowerIdAndFollowingId(follower.getUsername(), following.getUsername()))
                .willReturn(Optional.of(follow));

        //when
        followService.followUser(follower.getUsername(), following.getUsername(), follower);

        //then
        verify(followRepository, times(1)).findByFollowerIdAndFollowingId(anyString(), anyString());
        verify(followRepository, times(1)).delete(any(Follow.class));
    }

    @DisplayName("팔로워 목록 조회 - 성공")
    @Test
    void getFollowers() {
        //given
        User follower2 = User.builder()
                .username("follower2")
                .password("1234Aa@@")
                .name("팔로워2")
                .birthDate("19900903")
                .wantedJob("programmer")
                .profile(Profile
                        .builder()
                        .job("취업준비중")
                        .profileUrl("https://")
                        .build())
                .build();
        Follow follow1 = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        Follow follow2 = Follow.builder()
                .follower(follower2)
                .following(following)
                .build();
        List<Follow> follows = new ArrayList<>(List.of(follow1, follow2));
        Page<Follow> followPage = new PageImpl<>(follows);

        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.ofNullable(follower));
        given(followRepository.findAllByFollowing_Username(any(Pageable.class), anyString()))
                .willReturn(followPage);

        //when
        FollowersResponseDto followersResponseDto = followService.getFollowers(following.getUsername(), 1, 10);

        //then
        assertEquals(1, followersResponseDto.getPage().getTotalPage());

        List<ProfileResponseDto> followers = followersResponseDto.getFollowers();
        assertEquals(follower.getName(), followers.get(0).getName());
        assertEquals(follower.getUserJob(), followers.get(0).getJob());
        assertEquals(follower.getUserProfileUrl(), followers.get(0).getProfileUrl());
        assertEquals(follower2.getName(), followers.get(1).getName());
        assertEquals(follower2.getUserJob(), followers.get(1).getJob());
        assertEquals(follower2.getUserProfileUrl(), followers.get(1).getProfileUrl());
    }

    @DisplayName("팔로잉 목록 조회 - 성공")
    @Test
    void getFollowings() {
        User following2 = User.builder()
                .username("follower2")
                .password("1234Aa@@")
                .name("팔로워2")
                .birthDate("19900903")
                .wantedJob("programmer")
                .profile(Profile
                        .builder()
                        .job("취업준비중")
                        .profileUrl("https://")
                        .build())
                .build();
        Follow follow1 = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        Follow follow2 = Follow.builder()
                .follower(follower)
                .following(following2)
                .build();
        List<Follow> follows = new ArrayList<>(List.of(follow1, follow2));
        Page<Follow> followPage = new PageImpl<>(follows);

        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.ofNullable(follower));
        given(followRepository.findAllByFollower_Username(any(Pageable.class), anyString()))
                .willReturn(followPage);

        //when
        FollowingsResponseDto followingsResponseDto = followService.getFollowings(follower.getUsername(), 1, 10);

        //then
        assertEquals(1, followingsResponseDto.getPage().getTotalPage());
        List<ProfileResponseDto> followings = followingsResponseDto.getFollowings();
        assertEquals(following.getName(), followings.get(0).getName());
        assertEquals(following.getUserJob(), followings.get(0).getJob());
        assertEquals(following.getUserProfileUrl(), followings.get(0).getProfileUrl());
        assertEquals(following2.getName(), followings.get(1).getName());
        assertEquals(following2.getUserJob(), followings.get(1).getJob());
        assertEquals(following2.getUserProfileUrl(), followings.get(1).getProfileUrl());
    }
}