package com.linkerbell.portradebackend.domain.faq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.faq.dto.CreateFaqRequestDto;
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
class FaqControllerTest {

    final String PREFIX_URI = "/api/v1/faqs";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("자주 묻는 질문 등록 API 실패 - 권한 없음")
    void createFaqApi_unAuthorizedUser() throws Exception {
        //given
        CreateFaqRequestDto createFaqRequestDto = CreateFaqRequestDto
                .builder()
                .title("아이디 혹은 비밀번호를 잊었습니다.")
                .content("아이디 혹은 비밀번호를 잊으신 경우, 아이디 및 비밀번호 찾기를 이용해 주세요.")
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createFaqRequestDto)));

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithMockPortradeAdmin
    @DisplayName("자주 묻는 질문 등록 API 성공 - 관리자 계정")
    void createFaqApi_admin() throws Exception {
        //given
        CreateFaqRequestDto createFaqRequestDto = CreateFaqRequestDto
                .builder()
                .title("아이디 혹은 비밀번호를 잊었습니다.")
                .content("아이디 혹은 비밀번호를 잊으신 경우, 아이디 및 비밀번호 찾기를 이용해 주세요.")
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createFaqRequestDto)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4L));
    }

    @Test
    @DisplayName("자주 묻는 질문 목록 조회 API 성공")
    void getFaqsApi() throws Exception {
        //given
        int page = 1;
        int size = 10;

        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "?page={page}&size={size}", page, size)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.faqs[0].id").value(3L))
                .andExpect(jsonPath("$.faqs[0].title").value("포트폴리오를 암호화해서 공유하고 싶어요."))
                .andExpect(jsonPath("$.faqs[0].content").value("공유하기 버튼을 누르시면 됩니다."))
                .andExpect(jsonPath("$.faqs[1].id").value(2L))
                .andExpect(jsonPath("$.faqs[1].title").value("포트폴리오를 비공개로 전환하고 싶어요."))
                .andExpect(jsonPath("$.faqs[1].content").value("비공개 탭을 누르시면 됩니다."))
                .andExpect(jsonPath("$.faqs[2].id").value(1L))
                .andExpect(jsonPath("$.faqs[2].title").value("포트폴리오 업로드는 어떻게 하나요?"))
                .andExpect(jsonPath("$.faqs[2].content").value("포트레이트는 간편하게 포트폴리오를 업로드할 수 있어요."))
                .andExpect(jsonPath("$.maxPage").value(1));
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("자주 묻는 질문 삭제 API 실패 - 로그인 한 유저, 권한 없음")
    void deleteFaqApi_unAuthorizedUser() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("자주 묻는 질문 삭제 API 실패 - 로그인 안한 유저, 권한 없음")
    void deleteFaqApi_anonymous() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockPortradeAdmin
    @DisplayName("자주 묻는 질문 삭제 API 성공 - 관리자 계정")
    void deleteFaqApi_admin() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isNoContent());
    }
}