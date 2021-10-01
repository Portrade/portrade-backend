package com.linkerbell.portradebackend.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UserControllerTest {

    final String PREFIX_URI = "/api/v1/users";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("회원가입 API 성공")
    @Test
    void signUpApi() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId("userid")
                .name("name1")
                .password("1234Aa@@")
                .wantedJob("marketing")
                .birthDate("12341010")
                .college("college1")
                .graduation(false)
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("userid"))
                .andExpect(jsonPath("$.name").value("name1"))
                .andDo(print());
    }

    @DisplayName("회원가입 API 실패 - 중복된 username")
    @Test
    void signUpApi_duplicatedUsername() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId("user1")
                .name("name1")
                .password("1234Aa@@")
                .wantedJob("marketing")
                .birthDate("12341010")
                .college("college1")
                .graduation(false)
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("M010"))
                .andDo(print());
    }

    @DisplayName("회원가입 API 실패 - NULL USER NAME")
    @Test
    void signUpApi_NULL_USER_NAME() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId("userid2")
                .password("1234Aa@@")
                .wantedJob("marketing")
                .birthDate("12341010")
                .college("college1")
                .graduation(false)
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("M004"))
                .andDo(print());
    }
}