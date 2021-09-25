package com.linkerbell.portradebackend.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.user.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @DisplayName("사용자 인증 확인 API")
    @WithMockUser(username = "username1", password = "1234Aa@@", roles = "USER")
    @Test
    void checkAuthenticationApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get("/api/v1/auth/user"));

        // then
        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("사용자 인증 확인 API - 비로그인")
    @WithAnonymousUser
    @Test
    void checkAuthenticationApi_notLoggedIn() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get("/api/v1/auth/user"));

        // then
        result.andExpect(status().isUnauthorized())
                .andExpect(status().reason(containsString("로그인 정보가 없습니다.")))
                .andDo(print());
    }

    @DisplayName("사용자 인가 확인 API")
    @WithMockUser(username = "username1", password = "1234Aa@@", roles = {"USER", "ADMIN"})
    @Test
    void checkAuthorizationApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get("/api/v1/auth/admin"));

        // then
        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("사용자 인가 확인 API - 일반 회원")
    @WithMockUser(username = "username1", password = "1234Aa@@", roles = "USER")
    @Test
    void checkAuthorizationApi_notAdmin() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get("/api/v1/auth/admin"));

        // then
        result.andExpect(status().isForbidden())
                .andExpect(status().reason(containsString("권한이 없습니다.")))
                .andDo(print());
    }
}