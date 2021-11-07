package com.linkerbell.portradebackend.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.comment.dto.CommentRequestDto;
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
class CommentControllerTest {

    final String PREFIX_URI = "/api/v1/comments";

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

    @DisplayName("댓글 등록 API 성공")
    @Test
    @WithMockPortradeUser
    void writeCommentApi() throws Exception {
        // given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("댓글 내용")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(commentRequestDto)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("3"));
    }

    @DisplayName("댓글 등록 API 실패 - 비로그인")
    @Test
    void writeCommentApi_unAuthenticatedUser() throws Exception {
        // given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("댓글 내용")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(commentRequestDto)));

        // then
        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("C001"));
    }

    @DisplayName("댓글 등록 API 실패 - 존재하지 않는 포트폴리오 ID")
    @Test
    @WithMockPortradeUser
    void writeCommentApi_nonexistentPortfolioId() throws Exception {
        // given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("댓글 내용")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/123")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(commentRequestDto)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("P001"));
    }

    @DisplayName("댓글 등록 API 실패 - 내용 없음")
    @Test
    @WithMockPortradeUser
    void writeCommentApi_nullContent() throws Exception {
        // given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(commentRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("E100"));
    }

    @DisplayName("댓글 목록 조회 API 성공")
    @Test
    @WithMockPortradeUser
    void getCommentsApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.page.totalElement").value("2"));
    }

    @DisplayName("댓글 삭제 API 성공 - 작성자")
    @Test
    @WithMockPortradeUser
    void deleteCommentApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1/1"));

        // then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("댓글 삭제 API 성공 - 관리자")
    @Test
    @WithMockPortradeAdmin
    void deleteCommentApi_admin() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1/2"));

        // then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("댓글 삭제 API 실패 - 존재하지 않는 포트폴리오 ID")
    @Test
    @WithMockPortradeUser
    void deleteCommentApi_nonexistentPortfolioId() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/156/1"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("P001"));
    }

    @DisplayName("댓글 삭제 API 실패 - 존재하지 않는 댓글 ID")
    @Test
    @WithMockPortradeUser
    void deleteCommentApi_nonexistentCommentId() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1/15"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("E001"));
    }

    @DisplayName("댓글 삭제 API 실패 - 비로그인")
    @Test
    void deleteCommentApi_notLoggedIn() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1/1"));

        // then
        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("C001"));
    }

    @DisplayName("댓글 삭제 API 실패 - 권한없는 사용자")
    @Test
    @WithMockPortradeUser
    void deleteCommentApi_unauthorizedUser() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1/2"));

        // then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("C002"));
    }
}