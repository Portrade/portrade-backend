package com.linkerbell.portradebackend.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.user.dto.LogInRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthControllerTest {

    final String PREFIX_URI = "/api/v1/auth";

    @Value("${USER_ACCESS_TOKEN}")
    private String userAccessToken;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("로그인 API 성공")
    @Test
    void logInApi() throws Exception {
        // given
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .userId("user1")
                .password("1234Aa@@")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(logInRequestDto)));

        // then
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("로그인 API 실패 - 아이디 또는 비밀번호 틀림")
    @Test
    void logInApi_wrongLoginInfo() throws Exception {
        // given
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .userId("user12")
                .password("1234A@")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(logInRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("M012"))
                .andDo(print());
    }

    @DisplayName("로그아웃 API 성공")
    @Test
    void logOutApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/logout")
                .header("Authorization", userAccessToken));

        // then
        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("로그아웃 API 실패 - 비로그인")
    @Test
    void logOutApi_notLoggedIn() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/logout"));

        // then
        result.andExpect(status().isUnauthorized())
                .andDo(print());
    }
}