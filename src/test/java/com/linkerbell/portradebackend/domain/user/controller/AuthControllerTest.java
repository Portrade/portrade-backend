package com.linkerbell.portradebackend.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.user.dto.LogInRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class AuthControllerTest {

    final String PREFIX_URI = "/api/v1/auth";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @DisplayName("????????? API ??????")
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
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @DisplayName("????????? API ?????? - ????????? ????????? ?????? ????????????")
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
                .andExpect(jsonPath("$.code").value("M200"));
    }

    @DisplayName("???????????? API ??????")
    @Test
    @WithMockPortradeUser
    void logOutApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/logout"));

        // then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("???????????? API ?????? - ????????????")
    @Test
    void logOutApi_notLoggedIn() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/logout"));

        // then
        result.andExpect(status().isUnauthorized());
    }
}