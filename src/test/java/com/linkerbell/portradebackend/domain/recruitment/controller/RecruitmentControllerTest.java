package com.linkerbell.portradebackend.domain.recruitment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.recruitment.dto.RecruitmentRequestDto;
import com.linkerbell.portradebackend.global.config.WithMockPortradeAdmin;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class RecruitmentControllerTest {

    final String PREFIX_URI = "/api/v1/recruitments";

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

    @DisplayName("기업 공고 등록 API 성공")
    @Test
    @WithMockPortradeAdmin
    void createRecruitmentApi() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("공고 제목")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("금융 지원 서비스업")
                .pay("2억")
                .address("서울시 강남구 테헤란로")
                .category("PROGRAMMING")
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(recruitmentRequestDto)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("5"));
    }

    @DisplayName("기업 공고 등록 API 실패 - 비로그인")
    @Test
    void createRecruitmentApi_notLoggedIn() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("공고 제목")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("금융 지원 서비스업")
                .pay("2억")
                .address("서울시 강남구 테헤란로")
                .category("PROGRAMMING")
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/2")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(recruitmentRequestDto)));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @DisplayName("기업 공고 상세 조회 API 성공")
    @Test
    void getRecruitmentDetailApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/2"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.recruitment.address").value("서울특별시 강남구 테헤란로11길"))
                .andExpect(jsonPath("$.recruitment.career").value("웹 프로그래머"))
                .andExpect(jsonPath("$.recruitment.category").value("programmer"))
                .andExpect(jsonPath("$.recruitment.education").value("신입/경력"))
                .andExpect(jsonPath("$.recruitment.viewCount").value("11"));
    }

    @DisplayName("기업 공고 수정 API 실패 - 권한없는 사용자")
    @Test
    @WithMockPortradeUser
    void editRecruitmentApi_unAuthorizedUser() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("공고 제목")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("금융 지원 서비스업")
                .pay("2억")
                .address("서울시 강남구 테헤란로")
                .category("PROGRAMMING")
                .build();

        //when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(recruitmentRequestDto)));

        //then
        result.andExpect(status().isForbidden());
    }

    @DisplayName("기업 공고 수정 API 성공")
    @Test
    @WithMockPortradeAdmin
    void editRecruitmentApi() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("공고 제목")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("금융 지원 서비스업")
                .pay("2억")
                .address("서울시 강남구 테헤란로")
                .category("PROGRAMMING")
                .build();

        //when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/4")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(recruitmentRequestDto)));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("4"));
    }

    @DisplayName("기업 공고 수정 API 실패 - 비로그인")
    @Test
    void editRecruitmentApi_notLoggedIn() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("공고 제목")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("금융 지원 서비스업")
                .pay("2억")
                .address("서울시 강남구 테헤란로")
                .category("PROGRAMMING")
                .build();

        //when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/4")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(recruitmentRequestDto)));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @DisplayName("기업 공고 목록 조회 API 성공")
    @Test
    void getRecruitmentsApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.recruitments.size()").value("4"));
    }

    @DisplayName("기업 공고 삭제 API 성공")
    @Test
    @WithMockPortradeAdmin
    void deleteRecruitmentApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("기업 공고 삭제 API 실패 - 권한없는 사용자")
    @Test
    @WithMockPortradeUser
    void deleteRecruitmentApi_unAuthorizedUser() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isForbidden());
    }
}