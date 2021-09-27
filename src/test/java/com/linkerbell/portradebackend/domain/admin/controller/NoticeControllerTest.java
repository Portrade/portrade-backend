package com.linkerbell.portradebackend.domain.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeRequestDto;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.config.CustomSecurityFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class NoticeControllerTest {

    final String PREFIX_URI = "/api/v1/notices";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User admin;
    private User user;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new CustomSecurityFilter()))
                .build();

        admin = userRepository.findByUsername("admin1").get();
        user = userRepository.findByUsername("user1").get();
    }

    @DisplayName("공지사항 등록 API")
    @Test
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
                .principal(new UsernamePasswordAuthenticationToken(admin, null))
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.creator").value("admin1"))
                .andExpect(jsonPath("$.title").value("공지사항 제목"))
                .andExpect(jsonPath("$.viewCount").value(0))
                .andDo(print());
    }

    @DisplayName("공지사항 등록 API - 권한 없는 사용자")
    @Test
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
                .principal(new UsernamePasswordAuthenticationToken(user, null))
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isForbidden())
                .andDo(print());
    }

/* TODO
    @DisplayName("공지사항 상세 조회 API")
    @Test
    void getNoticeApi() throws Exception {
        // given
        Notice notice = Notice.builder()
                .id(1L)
                .user(admin)
                .title("공지사항 제목1")
                .content("공지사항 내용1")
                .viewCount(10)
                .build();

        // when
        ResultActions result = mvc.perform(get(PREFIX_URI));

        // then
        result.andExpect(status().isOk())
                .andDo(print());
    }*/

//    @DisplayName("공지사항 상세 조회 API - 존재하지 않는 id")
//    @DisplayName("공지사항 목록 조회 API")
}
