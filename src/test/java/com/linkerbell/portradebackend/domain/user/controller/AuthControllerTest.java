package com.linkerbell.portradebackend.domain.user.controller;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.LogInRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.TokenResponseDto;
import com.linkerbell.portradebackend.domain.user.service.AuthService;
import com.linkerbell.portradebackend.global.config.SecurityConfig;
import com.linkerbell.portradebackend.global.config.security.CustomAccessDeniedHandler;
import com.linkerbell.portradebackend.global.config.security.CustomAuthenticationEntryPoint;
import com.linkerbell.portradebackend.global.config.security.jwt.JwtFilter;
import com.linkerbell.portradebackend.global.config.security.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class,
        excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtFilter jwtFilter;

    @Spy
    private TokenProvider tokenProvider;

    @MockBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Test
    @DisplayName("login 테스트")
    public void checkUserRoleApi() {
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .userId("test")
                .password("Lm11122!")
                .build();

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                        .build();

        doReturn(tokenResponseDto).when(authService).logIn(logInRequestDto);
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

//    @DisplayName("사용자 인가 확인 API - 일반 회원이 어드민 회원에 접근")
//    @WithMockUser(roles = "USER")
//    @Test
//    void checkAuthorizationApi_notAdmin() throws Exception {
//        // given
//        String url = "/api/v1/auth/admin";
//        // when
//        final ResultActions resultActions = mockMvc.perform(
//                get(url)
//                        .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        resultActions.andDo(print());
//        resultActions.andExpect(status().isForbidden());
//        resultActions.andExpect(status().reason(containsString("권한이 없습니다.")));
//    }

    //test 실패, jwt token을 생성 못함 -> 통합테스트 가야할 거 같아요,,,
    @Test
    @DisplayName("사용자 인가 확인 API - 일반 회원이 어드민 회원에 접근")
    void checkAuthorizationApi_notAdmin() throws Exception {
        User user = User.builder()
                .username("username")
                .password("1111")
                .build();

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),Collections.singleton(grantedAuthority));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + makeJwtAuthToken(authentication));

        String url = "/api/v1/auth/admin";

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .headers(httpHeaders)
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