package com.linkerbell.portradebackend.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.UserResponseDto;
import com.linkerbell.portradebackend.domain.user.service.UserService;
import com.linkerbell.portradebackend.global.config.security.CustomAccessDeniedHandler;
import com.linkerbell.portradebackend.global.config.security.CustomAuthenticationEntryPoint;
import com.linkerbell.portradebackend.global.config.security.jwt.JwtFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    static String username = "username1";
    static String password = "1234Aa@@";
    static String name = "name1";
    static String birthDate = "12341010";
    static String wantedJob = "marketing";
    static String college = "college1";
    static boolean isGraduated = false;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @MockBean
    private JwtFilter jwtFilter;

    @DisplayName("회원가입 API")
    @Test
    void createUserApi() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId(username)
                .name(name)
                .password(password)
                .wantedJob(wantedJob)
                .birthDate(birthDate)
                .college(college)
                .graduation(isGraduated)
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .userId(username)
                .name(name)
                .build();

        given(userService.createUser(any(SignUpRequestDto.class)))
                .willReturn(userResponseDto);

        // when
        ResultActions result = mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(username))
                .andExpect(jsonPath("$.name").value(name))
                .andDo(print());
    }

    @DisplayName("회원가입 API - 중복된 username")
    @Test
    void createUserApi_duplicatedUsername() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId(username)
                .name(name)
                .password(password)
                .wantedJob(wantedJob)
                .birthDate(birthDate)
                .college(college)
                .graduation(isGraduated)
                .build();

        given(userService.createUser(any(SignUpRequestDto.class)))
                .willThrow(new IllegalArgumentException());

        // when
        ResultActions result = mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("회원가입 API - 유효성 검사")
    @Test
    void createUserApi_checkValidity() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .userId(username)
                .password("asdd")
                .wantedJob(wantedJob)
                .build();

        // when
        ResultActions result = mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(signUpRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andDo(print());
    }
}