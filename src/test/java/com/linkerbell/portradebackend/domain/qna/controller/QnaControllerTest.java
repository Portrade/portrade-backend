package com.linkerbell.portradebackend.domain.qna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.qna.dto.AnswerRequestDto;
import com.linkerbell.portradebackend.domain.qna.dto.QuestionRequestDto;
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

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class QnaControllerTest {

    final String PREFIX_URI = "/api/v1/qnas";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @DisplayName("1:1 문의 등록 API 실패 - 로그인 안함")
    @Test
    public void saveQnaApi_anonymous() throws Exception {
        //given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .category("업로드")
                .name("김질문")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .title("질문")
                .content("질문있어요.")
                .isPublic(false)
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(questionRequestDto))
        );

        //then
        result.andExpect(status().isUnauthorized());
    }

    @DisplayName("1:1 문의 등록 API 성공 - 로그인 한 유저")
    @Test
    @WithMockPortradeUser
    public void saveQnaApi() throws Exception {
        //given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .category("업로드")
                .name("김질문")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .title("질문")
                .content("질문있어요.")
                .isPublic(false)
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(questionRequestDto))
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @DisplayName("1:1 문의 등록 API 성공 - 어드민 계정")
    @Test
    @WithMockPortradeAdmin
    public void saveQnaApi_admin() throws Exception {
        //given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .category("업로드")
                .name("김질문")
                .email("admin1@gmail.com")
                .phoneNumber("12341234")
                .title("관리자도 질문 작성은 가능하다.")
                .content("관리자도 질문 작성은 가능하다.")
                .isPublic(false)
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(questionRequestDto))
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @DisplayName("1:1 문의 답변 글 작성 API 실패 - 유저가 작성")
    @Test
    @WithMockPortradeUser
    public void replyQnaApi_user() throws Exception {
        //given
        AnswerRequestDto answerRequestDto = AnswerRequestDto.builder()
                .title("1:1 답변글")
                .content("1:1 답변글 남깁니다.")
                .isPublic(false)
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(answerRequestDto)));

        //then
        result.andExpect(status().isForbidden());
    }

    @DisplayName("1:1 문의 답변 글 작성 API 성공 - 관리자가 작성")
    @Test
    @WithMockPortradeAdmin
    public void replyQnaApi_admin() throws Exception {
        //given
        AnswerRequestDto answerRequestDto = AnswerRequestDto.builder()
                .title("1:1 답변글")
                .content("1:1 답변글 남깁니다.")
                .isPublic(false)
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(answerRequestDto)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @DisplayName("1:1 문의 글 목록 조회 API 성공")
    @Test
    public void getQnasApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.qnas.size()").value("4"))
                .andExpect(jsonPath("$.qnas[0].id").value(4L))
                .andExpect(jsonPath("$.qnas[1].id").value(3L))
                .andExpect(jsonPath("$.qnas[2].id").value(2L))
                .andExpect(jsonPath("$.qnas[3].id").value(1L))
                .andExpect(jsonPath("$.qnas[0].title").value("1:1 문의합니다."))
                .andExpect(jsonPath("$.qnas[1].title").value("1:1 답변해드립니다."))
                .andExpect(jsonPath("$.qnas[2].title").value("1:1 문의합니다."))
                .andExpect(jsonPath("$.qnas[3].title").value("1:1 문의합니다."));
    }

    @DisplayName("1:1 문의 글 상세 조회 API 실패 - 조회 게시글 없는 경우")
    @Test
    public void getQnaDetailApi_nonexistentId() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1230"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("Q001"));
    }


    @DisplayName("1:1 문의 글 상세 조회 API 실패 - 로그인 안한 유저가 비공개 글 상세 조회")
    @Test
    public void getQnaDetailApi_anonymous() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("C002"));
    }

    @DisplayName("1:1 문의 글 상세 조회 API 실패 - 권한 없는 유저가 비공개 글 상세 조회")
    @Test
    @WithMockPortradeUser
    public void getQnaDetailApi_noauthentication() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/4"));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("C002"));
    }

    @DisplayName("1:1 문의 글 상세 조회 API 성공 - 글쓴이가 비공개 글 상세 조회")
    @Test
    @WithMockPortradeUser
    public void getQnaDetailApi_user() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isOk());
    }

    @DisplayName("1:1 문의 글 상세 조회 API 성공 - 관리자가 비공개 글 상세 조회")
    @Test
    @WithMockPortradeAdmin
    public void getQnaDetailApi_admin() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.creator").value("김가입"))
                .andExpect(jsonPath("$.title").value("1:1 문의합니다."))
                .andExpect(jsonPath("$.content").value("이력서 업로드 문의합니다."))
                .andExpect(jsonPath("$.isPublic").value(false))
                .andExpect(jsonPath("$.next.title").value("1:1 문의합니다."))
                .andExpect(jsonPath("$.prev").doesNotExist());
    }

    @DisplayName("1:1 문의 글 삭제 API 실패 - 권한 없는 사용자가 삭제")
    @Test
    @WithMockPortradeUser
    public void deleteQnaApi_noauthentication() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/3"));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("C002"));
    }

    @DisplayName("1:1 문의 글 삭제 API 실패 - 비로그인")
    @Test
    public void deleteQnaApi_notLoggedIn() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().is4xxClientError());
    }

    @DisplayName("1:1 문의 글 삭제 API 성공 - 글쓴이가 삭제")
    @Test
    @WithMockPortradeUser
    public void deleteQnaApi_creator() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("1:1 문의 글 삭제 API 성공 - 관리자가 삭제")
    @Test
    @WithMockPortradeAdmin
    public void deleteQnaApi_admin() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isNoContent());
    }
}