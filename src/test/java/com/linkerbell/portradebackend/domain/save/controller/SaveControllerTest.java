package com.linkerbell.portradebackend.domain.save.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class SaveControllerTest {

    final String PREFIX_URI = "/api/v1/save";

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

    @Test
    @DisplayName("저장된 포트폴리오 목록 조회 API 실패 - 비로그인")
    void getSavedPortfoliosApi_unAuthenticatedUser() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/portfolios"));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("저장된 포트폴리오 목록 조회 API 성공")
    void getSavedPortfoliosApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/portfolios"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolios[0].id").isNotEmpty())
                .andExpect(jsonPath("$.portfolios[0].title").value("user3의 포트폴리오"))
                .andExpect(jsonPath("$.portfolios[0].createdDate").value("2019-12-21T08:17:09.478881"));
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("저장된 포트폴리오 목록 조회 API 성공 - 빈 페이지네이션")
    void getSavedPortfoliosApi_empty() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/portfolios?page=2&size=10"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("1"))
                .andExpect(jsonPath("$.portfolios.size()").value("0"));
    }

    @Test
    @DisplayName("저장된 기업 공고 목록 조회 API 실패 - 비로그인")
    void getSavedRecruitmentsApi_unAuthenticatedUser() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/recruitments"));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("저장된 기업 공고 목록 조회 API 성공")
    void getSavedRecruitmentsApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/recruitments"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("1"))
                .andExpect(jsonPath("$.recruitments.size()").value("1"))
                .andExpect(jsonPath("$.recruitments[0].logo").value("http://www.aws1243.com"))
                .andExpect(jsonPath("$.recruitments[0].title").value("29샵 백엔드 프로그래머 채용"))
                .andExpect(jsonPath("$.recruitments[0].companyName").value("29샵"))
                .andExpect(jsonPath("$.recruitments[0].career").value("백엔드 프로그래머"))
                .andExpect(jsonPath("$.recruitments[0].education").value("신입/경력"))
                .andExpect(jsonPath("$.recruitments[0].address").value("서울특별시 강남구 테헤란로11길"));
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("저장된 기업 공고 목록 조회 API 성공 - 빈 페이지네이션")
    void getSavedRecruitmentsApi_empty() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/recruitments?page=2&size=10"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("1"))
                .andExpect(jsonPath("$.recruitments.size()").value("0"));
    }
}