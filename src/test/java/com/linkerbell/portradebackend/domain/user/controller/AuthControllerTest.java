package com.linkerbell.portradebackend.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.LogInRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.TokenResponseDto;
import com.linkerbell.portradebackend.domain.user.service.AuthService;

import com.linkerbell.portradebackend.global.config.security.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("login 테스트")
    public void checkUserRoleApi() throws Exception {
        String url = "/api/v1/auth/login";

        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .userId("test")
                .password("Lm11122!")
                .build();

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(logInRequestDto.getUserId(), logInRequestDto.getPassword(), Collections.singleton(grantedAuthority));

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(makeJwtAuthToken(authentication))
                .build();

        doReturn(tokenResponseDto).when(authService).logIn(logInRequestDto);

        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(logInRequestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
        assertThat(tokenResponseDto.getAccessToken()).isNotNull();
    }

    @DisplayName("사용자 인증 확인 API - 비로그인")
    @WithAnonymousUser
    @Test
    void checkAuthenticationApi_notLoggedIn() throws Exception {
        // given
        String url = "/api/v1/auth/user";
        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isUnauthorized());
        //resultActions.andExpect(status().reason(containsString("로그인 정보가 없습니다.")));
    }

    @DisplayName("사용자 인가 확인 API")
    @WithMockUser(username = "username1", password = "1234Aa@@", roles = "USER")
    @Test
    void checkAuthorizationApi_user() throws Exception {
        // given
        String url = "/api/v1/auth/user";

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url));

        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isNoContent());
    }

    @DisplayName("사용자 인가 확인 API - 일반 회원이 어드민 회원에 접근")
    @WithMockUser(username = "username1", password = "1234Aa@@", roles = "USER")
    @Test
    void checkAuthorizationApi_notAdmin() throws Exception {
        // given
        String url = "/api/v1/auth/admin";
        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(status().reason(containsString("권한이 없습니다.")));
    }

    @Test
    @DisplayName("사용자 인가 확인 API - 일반 회원이 어드민 회원에 접근")
    void checkAuthorizationApi_notAdmin2() throws Exception {
        User user = User.builder()
                .username("username")
                .password("1111")
                .build();

        String url = "/api/v1/auth/admin";

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)

                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print());
        resultActions.andExpect(status().isForbidden());
        //resultActions.andExpect(status().reason(containsString("권한이 없습니다.")));
    }


    private String makeJwtAuthToken(Authentication authentication) {
        log.info("{}", authentication.getAuthorities());
        return tokenProvider.createAccessToken(authentication);
    }
}