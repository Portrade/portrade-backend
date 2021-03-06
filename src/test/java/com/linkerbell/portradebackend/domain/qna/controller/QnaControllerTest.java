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

    @DisplayName("1:1 ?????? ?????? API ?????? - ????????? ??????")
    @Test
    public void saveQnaApi_anonymous() throws Exception {
        //given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .category("?????????")
                .name("?????????")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .title("??????")
                .content("???????????????.")
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

    @DisplayName("1:1 ?????? ?????? API ?????? - ????????? ??? ??????")
    @Test
    @WithMockPortradeUser
    public void saveQnaApi() throws Exception {
        //given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .category("?????????")
                .name("?????????")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .title("??????")
                .content("???????????????.")
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

    @DisplayName("1:1 ?????? ?????? API ?????? - ????????? ??????")
    @Test
    @WithMockPortradeAdmin
    public void saveQnaApi_admin() throws Exception {
        //given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
                .category("?????????")
                .name("?????????")
                .email("admin1@gmail.com")
                .phoneNumber("12341234")
                .title("???????????? ?????? ????????? ????????????.")
                .content("???????????? ?????? ????????? ????????????.")
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

    @DisplayName("1:1 ?????? ?????? ??? ?????? API ?????? - ????????? ??????")
    @Test
    @WithMockPortradeUser
    public void replyQnaApi_user() throws Exception {
        //given
        AnswerRequestDto answerRequestDto = AnswerRequestDto.builder()
                .title("1:1 ?????????")
                .content("1:1 ????????? ????????????.")
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

    @DisplayName("1:1 ?????? ?????? ??? ?????? API ?????? - ???????????? ??????")
    @Test
    @WithMockPortradeAdmin
    public void replyQnaApi_admin() throws Exception {
        //given
        AnswerRequestDto answerRequestDto = AnswerRequestDto.builder()
                .title("1:1 ?????????")
                .content("1:1 ????????? ????????????.")
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

    @DisplayName("1:1 ?????? ??? ?????? ?????? API ??????")
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
                .andExpect(jsonPath("$.qnas[0].title").value("1:1 ???????????????."))
                .andExpect(jsonPath("$.qnas[1].title").value("1:1 ?????????????????????."))
                .andExpect(jsonPath("$.qnas[2].title").value("1:1 ???????????????."))
                .andExpect(jsonPath("$.qnas[3].title").value("1:1 ???????????????."));
    }

    @DisplayName("1:1 ?????? ??? ?????? ?????? API ?????? - ?????? ????????? ?????? ??????")
    @Test
    public void getQnaDetailApi_nonexistentId() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1230"));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("Q001"));
    }


    @DisplayName("1:1 ?????? ??? ?????? ?????? API ?????? - ????????? ?????? ????????? ????????? ??? ?????? ??????")
    @Test
    public void getQnaDetailApi_anonymous() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("C002"));
    }

    @DisplayName("1:1 ?????? ??? ?????? ?????? API ?????? - ???????????? ????????? ????????? ??? ?????? ??????")
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

    @DisplayName("1:1 ?????? ??? ?????? ?????? API ?????? - ???????????? ????????? ??? ?????? ??????")
    @Test
    @WithMockPortradeUser
    public void getQnaDetailApi_user() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isOk());
    }

    @DisplayName("1:1 ?????? ??? ?????? ?????? API ?????? - ???????????? ????????? ??? ?????? ??????")
    @Test
    @WithMockPortradeAdmin
    public void getQnaDetailApi_admin() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.creator").value("?????????"))
                .andExpect(jsonPath("$.title").value("1:1 ???????????????."))
                .andExpect(jsonPath("$.content").value("????????? ????????? ???????????????."))
                .andExpect(jsonPath("$.isPublic").value(false))
                .andExpect(jsonPath("$.next.title").value("1:1 ???????????????."))
                .andExpect(jsonPath("$.prev").doesNotExist());
    }

    @DisplayName("1:1 ?????? ??? ?????? API ?????? - ???????????? ???????????? ??????")
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

    @DisplayName("1:1 ?????? ??? ?????? API ?????? - ????????????")
    @Test
    public void deleteQnaApi_notLoggedIn() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().is4xxClientError());
    }

    @DisplayName("1:1 ?????? ??? ?????? API ?????? - ???????????? ??????")
    @Test
    @WithMockPortradeUser
    public void deleteQnaApi_creator() throws Exception {
        //given
        //when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        //then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("1:1 ?????? ??? ?????? API ?????? - ???????????? ??????")
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