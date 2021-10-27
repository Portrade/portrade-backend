package com.linkerbell.portradebackend.domain.company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.company.dto.CompanyRequestDto;
import com.linkerbell.portradebackend.domain.company.dto.CreateCompanyRequestDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CompanyControllerTest {

    final String PREFIX_URI = "/api/v1/companies";

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
    @DisplayName("기업 등록 API 실패 - 비로그인")
    void createCompanyApi_unAuthenticatedUser() throws Exception {
        //given
        CreateCompanyRequestDto companyRequestDto = CreateCompanyRequestDto.builder()
                .name("(주)기업명")
                .form("중견기업")
                .industry("금용 지원 서비스업")
                .sales("1,1000억(2019년 기준)")
                .homepage("https://")
                .memberCount("1234명")
                .address("서울 금천구")
                .ceo("김주이")
                .foundingDate("2011년 2월 11일")
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(companyRequestDto)));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("기업 등록 API 성공")
    void createCompanyApi() throws Exception {
        //given
        CreateCompanyRequestDto companyRequestDto = CreateCompanyRequestDto.builder()
                .name("(주)기업명")
                .form("중견기업")
                .industry("금용 지원 서비스업")
                .sales("1,1000억(2019년 기준)")
                .homepage("https://")
                .memberCount("1234명")
                .address("서울 금천구")
                .ceo("김주이")
                .foundingDate("2011년 2월 11일")
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(companyRequestDto)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("5"));
    }

    @Test
    @DisplayName("기업 상세 조회 API 성공")
    void getCompanyDetailApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("지니프릭스"))
                .andExpect(jsonPath("$.form").value("스타트업"))
                .andExpect(jsonPath("$.industry").value("IT"))
                .andExpect(jsonPath("$.sales").value("12억"))
                .andExpect(jsonPath("$.homepage").value("https://www.gnifrix.com"))
                .andExpect(jsonPath("$.memberCount").value("124명"))
                .andExpect(jsonPath("$.address").value("서울특별시 금천구 가산디지털1로 2 (가산동) 1213호 (주)지니프릭스"))
                .andExpect(jsonPath("$.ceo").value("지니"))
                .andExpect(jsonPath("$.foundingDate").value("2014년 2월 8일"));
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("기업 수정 API 실패 - 권한 없는 사용자")
    void editCompanyApi_unAuthorizedUser() throws Exception {
        //given
        CompanyRequestDto companyRequestDto = CompanyRequestDto.builder()
                .name("(주)수정기업명")
                .form("중견기업")
                .industry("금용 지원 서비스업")
                .sales("1,1000억(2019년 기준)")
                .homepage("https://")
                .memberCount("1234명")
                .address("서울 금천구")
                .ceo("김주이")
                .foundingDate("2011년 2월 11일")
                .build();

        //when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(companyRequestDto)));

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("기업 수정 API 성공")
    void editCompanyApi() throws Exception {
        //given
        CompanyRequestDto companyRequestDto = CompanyRequestDto.builder()
                .name("(주)수정기업명")
                .form("중견기업")
                .industry("금용 지원 서비스업")
                .sales("1,1000억(2019년 기준)")
                .homepage("https://")
                .memberCount("1234명")
                .address("서울 금천구")
                .ceo("김주이")
                .foundingDate("2011년 2월 11일")
                .build();

        //when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/4")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(companyRequestDto)));

        //then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("기업의 모든 공고 조회 API 성공")
    void getRecruitmentsApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/4/recruitments"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.recruitments.size()").value("1"))
                .andExpect(jsonPath("$.maxPage").value("1"));
    }


    @Test
    @WithMockPortradeUser
    @DisplayName("기업 삭제 API 실패 - 권한 없는 사용자")
    void deleteCompanyApi_unAuthorizedUser() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("기업 삭제 API 성공")
    void deleteCompanyApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/4"));

        //then
        result.andExpect(status().isNoContent());
    }
}