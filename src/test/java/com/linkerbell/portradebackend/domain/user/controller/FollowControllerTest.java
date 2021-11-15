package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.user.domain.Follow;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.repository.FollowRepository;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.config.WithMockPortradeUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class FollowControllerTest {

    final String PREFIX_URI = "/api/v1/users";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @DisplayName("회원 팔로우/취소 API 성공 - 팔로우")
    @Test
    @WithMockPortradeUser
    void followApi_follow() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(patch(PREFIX_URI + "/admin1/follow")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isCreated());
    }

    @DisplayName("회원 팔로우/취소 API 성공 - 팔로우 취소")
    @Test
    @WithMockPortradeUser
    void followApi_cancel() throws Exception {
        //given
        User user1 = userRepository.findByUsername("user1").get();
        User user2 = userRepository.findByUsername("user2").get();

        Follow follow = Follow
                .builder()
                .follower(user1)
                .following(user2)
                .build();

        followRepository.save(follow);

        //when
        ResultActions result = mvc.perform(patch(PREFIX_URI + "/user2/follow")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("회원 팔로우/취소 API 실패 - 비로그인")
    @Test
    void followApi_notLoggedIn() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(patch(PREFIX_URI + "/user2/follow")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @DisplayName("팔로워 목록 조회 API 성공 - 빈 페이지 네이션")
    @Test
    void getFollowersApi_emptyPage() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/user1/followers?page=2&size=2"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.followers.size()").value("0"));
    }

    @DisplayName("팔로워 목록 조회 API 성공")
    @Test
    void getFollowersApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/user1/followers"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.followers.size()").value("1"))
                .andExpect(jsonPath("$.followers[0].id").isNotEmpty())
                .andExpect(jsonPath("$.followers[0].name").value("사나"))
                .andExpect(jsonPath("$.followers[0].job").value("jyp"))
                .andExpect(jsonPath("$.page.totalPage").value("1"));
    }

    @DisplayName("팔로워 목록 조회 API 실패 - 존재하지 않는 사용자")
    @Test
    void getFollowersApi_nonexistentUsername() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/user13/followers"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("M001"));
    }

    @DisplayName("팔로잉 목록 조회 API 성공 - 빈 페이지 네이션")
    @Test
    void getFollowingsApi_emptyPage() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/user1/followings?page=2&size=2"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.followings.size()").value("0"));
    }

    @DisplayName("팔로잉 목록 조회 API 성공")
    @Test
    void getFollowingsApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/user1/followings"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.followings.size()").value("1"))
                .andExpect(jsonPath("$.followings[0].id").isNotEmpty())
                .andExpect(jsonPath("$.followings[0].name").value("김유저"))
                .andExpect(jsonPath("$.followings[0].job").value("naver"))
                .andExpect(jsonPath("$.page.totalPage").value("1"));
    }

    @DisplayName("팔로잉 목록 조회 API 실패 - 존재하지 않는 사용자")
    @Test
    void getFollowingsApi_nonexistentUsername() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/user13/followings"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("M001"));
    }
}