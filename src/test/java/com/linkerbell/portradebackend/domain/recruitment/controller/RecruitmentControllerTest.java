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

    @DisplayName("?????? ?????? ?????? API ??????")
    @Test
    @WithMockPortradeAdmin
    void createRecruitmentApi() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("?????? ??????")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("?????? ?????? ????????????")
                .pay("2???")
                .address("????????? ????????? ????????????")
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

    @DisplayName("?????? ?????? ?????? API ?????? - ????????????")
    @Test
    void createRecruitmentApi_notLoggedIn() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("?????? ??????")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("?????? ?????? ????????????")
                .pay("2???")
                .address("????????? ????????? ????????????")
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

    @DisplayName("?????? ?????? ?????? ?????? API ??????")
    @Test
    void getRecruitmentDetailApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/2"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.recruitment.address").value("??????????????? ????????? ????????????11???"))
                .andExpect(jsonPath("$.recruitment.career").value("??? ???????????????"))
                .andExpect(jsonPath("$.recruitment.category").value("programmer"))
                .andExpect(jsonPath("$.recruitment.education").value("??????/??????"))
                .andExpect(jsonPath("$.recruitment.viewCount").value("11"));
    }

    @DisplayName("?????? ?????? ?????? API ?????? - ???????????? ?????????")
    @Test
    @WithMockPortradeUser
    void editRecruitmentApi_unAuthorizedUser() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("?????? ??????")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("?????? ?????? ????????????")
                .pay("2???")
                .address("????????? ????????? ????????????")
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

    @DisplayName("?????? ?????? ?????? API ??????")
    @Test
    @WithMockPortradeAdmin
    void editRecruitmentApi() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("?????? ??????")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("?????? ?????? ????????????")
                .pay("2???")
                .address("????????? ????????? ????????????")
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

    @DisplayName("?????? ?????? ?????? API ?????? - ????????????")
    @Test
    void editRecruitmentApi_notLoggedIn() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder()
                .logo("url.com")
                .title("?????? ??????")
                .career("EXPERIENCED")
                .education("HIGHSCHOOL")
                .workType("?????? ?????? ????????????")
                .pay("2???")
                .address("????????? ????????? ????????????")
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

    @DisplayName("?????? ?????? ?????? ?????? API ??????")
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

    @DisplayName("?????? ?????? ?????? API ??????")
    @Test
    @WithMockPortradeAdmin
    void deleteRecruitmentApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("?????? ?????? ?????? API ?????? - ???????????? ?????????")
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