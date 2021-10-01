package com.linkerbell.portradebackend.domain.qna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.qna.dto.CreateQnaRequestDto;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.config.CustomSecurityFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class QnaControllerTest {

    final String PREFIX_URI = "/api/v1/qnas";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    User user1;
    User admin1;


    @Test
    @DisplayName("1:1 문의 등록 API - 로그인 한 유저")
    public void saveQnaApi() throws Exception {
        user1 = userRepository.findByUsername("user1").get();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(new CustomSecurityFilter()))
                .build();
        //given
        CreateQnaRequestDto createQnaRequestDto = CreateQnaRequestDto.builder()
                .category("업로드")
                .name("김질문")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .title("질문")
                .content("질문있어요.")
                .isPublic(false)
                .build();
        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .principal(new UsernamePasswordAuthenticationToken(user1, null))
                .content(objectMapper.writeValueAsString(createQnaRequestDto))
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("1:1 문의 등록 API 실패 - 로그인 안함")
    public void saveQnaApi_anonymous() throws Exception {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        //given
        CreateQnaRequestDto createQnaRequestDto = CreateQnaRequestDto.builder()
                .category("업로드")
                .name("김질문")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .title("질문")
                .content("질문있어요.")
                .isPublic(false)
                .build();
        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createQnaRequestDto))
        );

        //then
        result.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("1:1 문의 등록 API - 어드민 계정")
    public void saveQnaApi_admin() throws Exception {
        admin1 = userRepository.findByUsername("admin1").get();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(new CustomSecurityFilter()))
                .build();
        //given
        CreateQnaRequestDto createQnaRequestDto = CreateQnaRequestDto.builder()
                .category("업로드")
                .name("김질문")
                .email("admin1@gmail.com")
                .phoneNumber("12341234")
                .title("관리자도 질문 작성은 가능하다.")
                .content("관리자도 질문 작성은 가능하다.")
                .isPublic(false)
                .build();
        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createQnaRequestDto))
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    //비공개 글 상세 조회 시도하는 api 테스트할 때 역할 나눠서 시도
    @Test
    @DisplayName("1:1 문의 글 - 글쓴이가 비공개 글 상세 조회")
    public void getQnaDetailApi_user() throws Exception {
        //given
        user1 = userRepository.findByUsername("user1").get();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(new CustomSecurityFilter()))
                .build();

        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1")
                .principal(new UsernamePasswordAuthenticationToken(user1, null)));
        //then
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("1:1 문의 글 - 로그인 안한 유저가 비공개 글 상세 조회")
    public void getQnaDetailApi_anonymous() throws Exception {
        //given
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));
        //then
        result.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("1:1 문의 글 - 로그인한 권한 없는 유저가 비공개 글 상세 조회")
    public void getQnaDetailApi_noauthentication() throws Exception {
        User user2 = userRepository.findByUsername("user2").get();
        //given
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(new CustomSecurityFilter()))
                .build();

        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1")
                .principal(new UsernamePasswordAuthenticationToken(user2, null)));
        //then
        result.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("M201"));
    }

    @Test
    @DisplayName("1:1 문의 글 - 관리자가 비공개 글 상세 조회")
    public void getQnaDetailApi_admin() throws Exception {
        admin1 = userRepository.findByUsername("admin1").get();
        //given
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(new CustomSecurityFilter()))
                .build();

        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1")
                .principal(new UsernamePasswordAuthenticationToken(admin1, null)));
        //then
        result.andDo(print())
                .andExpect(status().isOk());
    }
}