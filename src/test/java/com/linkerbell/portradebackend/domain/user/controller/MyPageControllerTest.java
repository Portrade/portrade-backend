package com.linkerbell.portradebackend.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.user.dto.JobRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileRequestDto;
import com.linkerbell.portradebackend.global.config.WithMockPortradeUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
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
class MyPageControllerTest {

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

    @Test
    @DisplayName("프로필 사진 업로드 API 실패 - 권한 없음")
    void uploadProfileImageApi_unAuthorizedUser() throws Exception {
        //given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "fileImage.jpeg",
                "image/jpeg",
                "fileImage".getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(PREFIX_URI + "/me/profile/image");

        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        //when
        ResultActions result = mvc.perform(builder
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("프로필 사진 업로드 API 성공")
    void uploadProfileImageApi() throws Exception {
        //given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "fileImage.jpeg",
                "image/jpeg",
                "fileImage".getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(PREFIX_URI + "/me/profile/image");

        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        //when
        ResultActions result = mvc.perform(builder
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("나의 인사이트 조회 API 성공")
    void getMyInsightApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(
                get(PREFIX_URI + "/me/insight"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.viewCount").value("400"))
                .andExpect(jsonPath("$.likes").value("3"))
                .andExpect(jsonPath("$.comment").value("2"))
                .andExpect(jsonPath("$.followers").value("1"))
                .andExpect(jsonPath("$.followings").value("1"));
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("프로필 편집 API 성공")
    void updateProfileApi() throws Exception {
        //given
        ProfileRequestDto profileRequestDto = ProfileRequestDto
                .builder()
                .name("김수정")
                .birthDate("19990909")
                .wantedJob("programmer")
                .build();

        //when
        ResultActions result = mvc.perform(
                put(PREFIX_URI + "/me/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequestDto)));

        //then
        result.andExpect(status().isNoContent());

    }

    @Test
    @WithMockPortradeUser
    @DisplayName("구직 상태 편집 API 성공")
    void updateProfileJobApi() throws Exception {
        //given
        JobRequestDto jobRequestDto = new JobRequestDto("naver");

        //when
        ResultActions result = mvc.perform(
                put(PREFIX_URI + "/me/profile/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobRequestDto)));

        //then
        result.andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("특정 사용자의 포트폴리오 목록 조회 API 성공")
    void getUserPortfoliosApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(
                get(PREFIX_URI + "/user1/portfolios"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("1"))
                .andExpect(jsonPath("$.portfolios.size()").value("3"))
                .andExpect(jsonPath("$.portfolios[0].title").value("포트폴리오3제목"))
                .andExpect(jsonPath("$.portfolios[0].createdDate").value("2019-10-21T08:17:09.478881"))
                .andExpect(jsonPath("$.portfolios[1].title").value("포트폴리오2제목"))
                .andExpect(jsonPath("$.portfolios[1].createdDate").value("2019-10-20T08:17:09.478881"))
                .andExpect(jsonPath("$.portfolios[2].title").value("포트폴리오1제목"))
                .andExpect(jsonPath("$.portfolios[2].createdDate").value("2019-10-19T08:17:09.478881"));
    }

    @Test
    @DisplayName("특정 사용자의 포트폴리오 목록 조회 API 성공 - 빈 페이지네이션")
    void getUserPortfoliosApi_empty() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(
                get(PREFIX_URI + "/user1/portfolios?page=2&size=10"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("1"))
                .andExpect(jsonPath("$.portfolios.size()").value("0"));
    }

    @Test
    @DisplayName("특정 사용자의 프로필 조회 API 성공")
    void getUserProfileApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(
                get(PREFIX_URI + "/user1/profile"));
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("김가입"))
                .andExpect(jsonPath("$.profileUrl").isEmpty())
                .andExpect(jsonPath("$.job").value("취업준비중"));
    }
}