package com.linkerbell.portradebackend.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
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
class UserControllerTest {

    final String PREFIX_URI = "/api/v1/users";

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

    @DisplayName("회원가입 API 성공")
    @Test
    void creatUserApi() throws Exception {
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
                .andExpect(jsonPath("$.name").value("name1"));
    }

    @DisplayName("회원가입 API 실패 - 중복된 username")
    @Test
    void creatUserApi_duplicatedUsername() throws Exception {
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
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("M109"));
    }

    @DisplayName("회원가입 API 실패 - NULL USER NAME")
    @Test
    void creatUserApi_NULL_USER_NAME() throws Exception {
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
                .andExpect(jsonPath("$.code").value("M103"));
    }

    @Test
    @DisplayName("아이디 중복 확인 API 실패 - 중복인 경우")
    void checkUsernameExistsApi_duplicate() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/user1/exist"));

        //then
        result.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("아이디 중복 확인 API 성공")
    void checkUsernameExistsApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/users/exist"));

        //then
        result.andExpect(status().isNoContent());
    }
}