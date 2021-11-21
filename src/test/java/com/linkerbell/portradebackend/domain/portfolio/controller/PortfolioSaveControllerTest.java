package com.linkerbell.portradebackend.domain.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.global.config.WithMockPortradeUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class PortfolioSaveControllerTest {

    final String PREFIX_URI = "/api/v1/portfolios";

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

    @DisplayName("포트폴리오 저장 API 성공")
    @Test
    @WithMockPortradeUser
    void savePortfolioApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(patch(PREFIX_URI + "/2/save"));

        // then
        result.andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("포트폴리오 저장 취소 API 성공")
    @Test
    @WithMockPortradeUser
    void cancelSavePortfolioApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(patch(PREFIX_URI + "/1/save"));

        // then
        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("포트폴리오 저장 API 실패 - 비로그인")
    @Test
    void savePortfolioApi_notLoggedIn() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(patch(PREFIX_URI + "/1/save"));

        // then
        result.andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("포트폴리오 저장 API 실패 - 존재하지 않는 포트폴리오 ID")
    @Test
    @WithMockPortradeUser
    void savePortfolioApi_nonexistentPortfolioId() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(patch(PREFIX_URI + "/10/save"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("P001"))
                .andDo(print());
    }

    @DisplayName("저장된 포트폴리오 목록 조회 API 성공")
    @Test
    @WithMockPortradeUser
    void getSavedPortfoliosApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/save"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.page.totalElement").value("1"))
                .andDo(print());
    }

    @DisplayName("저장된 포트폴리오 목록 조회 API 실패 - 비로그인")
    @Test
    void getSavedPortfoliosApi_notLoggedIn() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/save"));

        // then
        result.andExpect(status().isUnauthorized());
    }
}