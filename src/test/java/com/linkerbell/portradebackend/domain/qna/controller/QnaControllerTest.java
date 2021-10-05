package com.linkerbell.portradebackend.domain.qna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.qna.dto.CreateQnaRequestDto;
import com.linkerbell.portradebackend.domain.qna.dto.ReplyQnaRequestDto;
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

    @Test
    @DisplayName("1:1 문의 등록 API 실패 - 로그인 안함")
    public void saveQnaApi_anonymous() throws Exception {
        //given
        CreateQnaRequestDto createQnaRequestDto = CreateQnaRequestDto.builder()
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
                .content(objectMapper.writeValueAsString(createQnaRequestDto))
        );

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("1:1 문의 등록 API 성공 - 로그인 한 유저")
    public void saveQnaApi() throws Exception {
        //given
        CreateQnaRequestDto createQnaRequestDto = CreateQnaRequestDto.builder()
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
                .content(objectMapper.writeValueAsString(createQnaRequestDto))
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithMockPortradeAdmin
    @DisplayName("1:1 문의 등록 API 성공 - 어드민 계정")
    public void saveQnaApi_admin() throws Exception {
        //given
        CreateQnaRequestDto createQnaRequestDto = CreateQnaRequestDto.builder()
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
                .content(objectMapper.writeValueAsString(createQnaRequestDto))
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("1:1 문의 답변 글 작성 API 실패 - 유저가 작성")
    public void replyQnaApi_user() throws Exception {
        //given
        ReplyQnaRequestDto replyQnaRequestDto = ReplyQnaRequestDto.builder()
                .title("1:1 답변글")
                .content("1:1 답변글 남깁니다.")
                .secret(false)
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(replyQnaRequestDto)));

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithMockPortradeAdmin
    @DisplayName("1:1 문의 답변 글 작성 API 성공 - 관리자가 작성")
    public void replyQnaApi_admin() throws Exception {
        //given
        ReplyQnaRequestDto replyQnaRequestDto = ReplyQnaRequestDto.builder()
                .title("1:1 답변글")
                .content("1:1 답변글 남깁니다.")
                .secret(false)
                .build();

        //when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(replyQnaRequestDto)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("1:1 문의 글 목록 조회 API 성공")
    public void getQnasApi() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("1"))
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


    @Test
    @DisplayName("1:1 문의 글 상세 조회 API 실패 - 조회 게시글 없는 경우")
    public void getQnaDetailApi_nonexistentId() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1230"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("Q001"));
    }


    @Test
    @DisplayName("1:1 문의 글 상세 조회 API 실패 - 로그인 안한 유저가 비공개 글 상세 조회")
    public void getQnaDetailApi_anonymous() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("M001"));
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("1:1 문의 글 상세 조회 API 실패 - 권한 없는 유저가 비공개 글 상세 조회")
    public void getQnaDetailApi_noauthentication() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/4"));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("M001"));
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("1:1 문의 글 상세 조회 API 성공 - 글쓴이가 비공개 글 상세 조회")
    public void getQnaDetailApi_user() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @WithMockPortradeAdmin
    @DisplayName("1:1 문의 글 상세 조회 API 성공 - 관리자가 비공개 글 상세 조회")
    public void getQnaDetailApi_admin() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.creator").value("김가입"))
                .andExpect(jsonPath("$.title").value("1:1 문의합니다."))
                .andExpect(jsonPath("$.content").value("이력서 업로드 문의합니다."))
                .andExpect(jsonPath("$.secret").value(false))
                .andExpect(jsonPath("$.next.creator").value("사나"))
                .andExpect(jsonPath("$.next.title").value("1:1 문의합니다."))
                .andExpect(jsonPath("$.prev").doesNotExist());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("1:1 문의 글 삭제 API 실패 - 권한 없는 사용자가 삭제")
    public void deleteQnaApi_noauthentication() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/3"));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("M001"));
    }

    @Test
    @DisplayName("1:1 문의 글 삭제 API 실패 - 로그인 안한 사용자가 삭제")
    public void deleteQnaApi_anonymous() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockPortradeUser
    @DisplayName("1:1 문의 글 삭제 API 실패 - 글쓴이가 삭제")
    public void deleteQnaApi_user() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isNoContent());
    }
}