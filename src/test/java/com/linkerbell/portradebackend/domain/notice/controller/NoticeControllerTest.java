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

    @DisplayName("공지사항 등록 API 성공")
    @Test
    @WithMockPortradeAdmin
    void writeNoticeApi() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목")
                .content("공지사항 내용")
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

    @DisplayName("공지사항 등록 API 실패 - 권한 없는 사용자")
    @Test
    @WithMockPortradeUser
    void writeNoticeApi_unAuthorizedUser() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목")
                .content("공지사항 내용")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isForbidden());
    }

    @DisplayName("공지사항 등록 API 실패 - 비로그인")
    @Test
    void writeNoticeApi_unAuthenticatedUser() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목")
                .content("공지사항 내용")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isUnauthorized());
    }

    @DisplayName("공지사항 등록 API 실패 - 유효하지 않은 요청 값")
    @Test
    @WithMockPortradeAdmin
    void writeNoticeApi_invalidRequestBody() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목")
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

    @DisplayName("공지사항 목록 조회 API 성공")
    @Test
    void getNoticesApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("1"))
                .andExpect(jsonPath("$.notices.size()").value("3"))
                .andExpect(jsonPath("$.notices[0].title").value("[공지사항 분류]공지사항 제목입니다."))
                .andExpect(jsonPath("$.notices[1].title").value("[공지사항 분류]공지사항 제목입니다."))
                .andExpect(jsonPath("$.notices[2].title").value("[공지사항 분류]공지사항 제목입니다."))
                .andExpect(jsonPath("$.notices[0].viewCount").value("1500"))
                .andExpect(jsonPath("$.notices[1].viewCount").value("1220"))
                .andExpect(jsonPath("$.notices[2].viewCount").value("1200"));
    }

    @DisplayName("공지사항 목록 조회 API 성공 - page=1&size=2")
    @Test
    void getNoticesApi_size2() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "?page=1&size=2"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("2"))
                .andExpect(jsonPath("$.notices.size()").value("2"))
                .andExpect(jsonPath("$.notices[0].title").value("[공지사항 분류]공지사항 제목입니다."))
                .andExpect(jsonPath("$.notices[1].title").value("[공지사항 분류]공지사항 제목입니다."))
                .andExpect(jsonPath("$.notices[0].viewCount").value("1500"))
                .andExpect(jsonPath("$.notices[1].viewCount").value("1220"));
    }

    @DisplayName("공지사항 목록 조회 API 성공 - 빈 페이지네이션")
    @Test
    void getNoticesApi_empty() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "?page=2&size=5"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("1"))
                .andExpect(jsonPath("$.notices.size()").value("0"));
    }

    @DisplayName("공지사항 상세 조회 API 성공")
    @Test
    void getNoticeApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.creator").value("admin1"))
                .andExpect(jsonPath("$.title").value("[공지사항 분류]공지사항 제목입니다."))
                .andExpect(jsonPath("$.content").value("공지사항1 내용입니다."))
                .andExpect(jsonPath("$.viewCount").value("1201"));
    }

    @DisplayName("공지사항 상세 조회 API 실패 - 존재하지 않는 ID")
    @Test
    void getNoticeApi_nonexistentNoticeId() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1234"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("N001"));
    }

    @DisplayName("공지사항 수정 API 성공")
    @Test
    @WithMockPortradeAdmin
    void updateNoticeApi() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 수정 제목")
                .content("공지사항 수정 내용")
                .build();

        // when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("공지사항 수정 API 실패 - 유효하지 않은 요청 값")
    @Test
    @WithMockPortradeAdmin
    void updateNoticeApi_invalidRequestBody() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목")
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

    @DisplayName("공지사항 수정 API 실패 - 존재하지 않는 ID")
    @Test
    @WithMockPortradeAdmin
    void updateNoticeApi_nonexistentId() throws Exception {
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 수정 제목")
                .content("공지사항 수정 내용")
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

    @DisplayName("공지사항 삭제 API 성공")
    @Test
    @WithMockPortradeAdmin
    void deleteNoticeApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1"));

        // then
        result.andExpect(status().isNoContent());
    }

    @DisplayName("공지사항 삭제 API 실패 - 존재하지 않는 ID")
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

    @DisplayName("공지사항 삭제 API 실패 - 권한 없는 사용자")
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
