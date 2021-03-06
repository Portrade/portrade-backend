package com.linkerbell.portradebackend.domain.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.notice.dto.NoticeRequestDto;
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
class NoticeControllerTest {

    final String PREFIX_URI = "/api/v1/notices";

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

    @DisplayName("???????????? ?????? API ??????")
    @Test
    @WithMockPortradeAdmin
    void writeNoticeApi() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("???????????? ??????")
                .content("???????????? ??????")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("4"));
    }

    @DisplayName("???????????? ?????? API ?????? - ???????????? ?????????")
    @Test
    @WithMockPortradeUser
    void writeNoticeApi_unAuthorizedUser() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("???????????? ??????")
                .content("???????????? ??????")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isForbidden());
    }

    @DisplayName("???????????? ?????? API ?????? - ????????????")
    @Test
    void writeNoticeApi_unAuthenticatedUser() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("???????????? ??????")
                .content("???????????? ??????")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isUnauthorized());
    }

    @DisplayName("???????????? ?????? API ?????? - ???????????? ?????? ?????? ???")
    @Test
    @WithMockPortradeAdmin
    void writeNoticeApi_invalidRequestBody() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("???????????? ??????")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("N101"));
    }

    @DisplayName("???????????? ?????? ?????? API ??????")
    @Test
    void getNoticesApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.notices.size()").value("3"))
                .andExpect(jsonPath("$.notices[0].title").value("[???????????? ??????]???????????? ???????????????."))
                .andExpect(jsonPath("$.notices[1].title").value("[???????????? ??????]???????????? ???????????????."))
                .andExpect(jsonPath("$.notices[2].title").value("[???????????? ??????]???????????? ???????????????."))
                .andExpect(jsonPath("$.notices[0].viewCount").value("1500"))
                .andExpect(jsonPath("$.notices[1].viewCount").value("1220"))
                .andExpect(jsonPath("$.notices[2].viewCount").value("1200"));
    }

    @DisplayName("???????????? ?????? ?????? API ?????? - page=1&size=2")
    @Test
    void getNoticesApi_size2() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "?page=1&size=2"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("2"))
                .andExpect(jsonPath("$.notices.size()").value("2"))
                .andExpect(jsonPath("$.notices[0].title").value("[???????????? ??????]???????????? ???????????????."))
                .andExpect(jsonPath("$.notices[1].title").value("[???????????? ??????]???????????? ???????????????."))
                .andExpect(jsonPath("$.notices[0].viewCount").value("1500"))
                .andExpect(jsonPath("$.notices[1].viewCount").value("1220"));
    }

    @DisplayName("???????????? ?????? ?????? API ?????? - ??? ??????????????????")
    @Test
    void getNoticesApi_empty() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "?page=2&size=5"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.notices.size()").value("0"));
    }

    @DisplayName("???????????? ?????? ?????? API ??????")
    @Test
    void getNoticeApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.creator").value("admin1"))
                .andExpect(jsonPath("$.title").value("[???????????? ??????]???????????? ???????????????."))
                .andExpect(jsonPath("$.content").value("????????????1 ???????????????."))
                .andExpect(jsonPath("$.viewCount").value("1201"));
    }

    @DisplayName("???????????? ?????? ?????? API ?????? - ???????????? ?????? ID")
    @Test
    void getNoticeApi_nonexistentNoticeId() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1234"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("N001"));
    }

    @DisplayName("???????????? ?????? API ??????")
    @Test
    @WithMockPortradeAdmin
    void updateNoticeApi() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("???????????? ?????? ??????")
                .content("???????????? ?????? ??????")
                .build();

        // when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"));
    }

    @DisplayName("???????????? ?????? API ?????? - ???????????? ?????? ?????? ???")
    @Test
    @WithMockPortradeAdmin
    void updateNoticeApi_invalidRequestBody() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("???????????? ??????")
                .build();

        // when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("N101"));
    }

    @DisplayName("???????????? ?????? API ?????? - ???????????? ?????? ID")
    @Test
    @WithMockPortradeAdmin
    void updateNoticeApi_nonexistentId() throws Exception {
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("???????????? ?????? ??????")
                .content("???????????? ?????? ??????")
                .build();

        // when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/13")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("N001"));
    }

    @DisplayName("???????????? ?????? API ??????")
    @Test
    @WithMockPortradeAdmin
    void deleteNoticeApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        // then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("???????????? ?????? API ?????? - ???????????? ?????? ID")
    @Test
    @WithMockPortradeAdmin
    void deleteNoticeApi_nonexistentId() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/12"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("N001"));
    }

    @DisplayName("???????????? ?????? API ?????? - ???????????? ?????????")
    @Test
    @WithMockPortradeUser
    void deleteNoticeApi_unAuthorizedUser() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/2"));

        // then
        result.andExpect(status().isForbidden());
    }
}
