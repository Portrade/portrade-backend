package com.linkerbell.portradebackend.domain.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.notice.dto.NoticeRequestDto;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class NoticeControllerTest {

    final String PREFIX_URI = "/api/v1/notices";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    private User admin;
    private User user;

    @BeforeEach
    public void setUp() {
        admin = userRepository.findByUsername("admin1").get();
        user = userRepository.findByUsername("user1").get();
    }

    @Order(12)
    @DisplayName("공지사항 등록 API 성공")
    @WithMockUser(roles = "ADMIN")
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
                .andExpect(jsonPath("$.id").value("4"))
                .andDo(print());
    }

    @Order(13)
    @DisplayName("공지사항 등록 API 실패 - 권한 없는 사용자")
    @WithMockUser
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

    @Order(14)
    @DisplayName("공지사항 등록 API 실패 - 유효하지 않은 요청 값")
    @WithMockUser(roles = "ADMIN")
    @Test
    void writeNoticeApi_invalidRequestBody() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목")
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .principal(new UsernamePasswordAuthenticationToken(admin, null))
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("N101"))
                .andDo(print());
    }

    @Order(1)
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
                .andExpect(jsonPath("$.notices[2].viewCount").value("1200"))
                .andDo(print());
    }

    @Order(2)
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
                .andExpect(jsonPath("$.notices[1].viewCount").value("1220"))
                .andDo(print());
    }

    @Order(3)
    @DisplayName("공지사항 목록 조회 API 성공 - 빈 페이지네이션")
    @Test
    void getNoticesApi_empty() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "?page=2&size=5"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPage").value("1"))
                .andExpect(jsonPath("$.notices.size()").value("0"))
                .andDo(print());
    }

    @Order(4)
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
                .andExpect(jsonPath("$.viewCount").value("1201"))
                .andDo(print());
    }

    @Order(5)
    @DisplayName("공지사항 상세 조회 API 실패 - 존재하지 않는 ID")
    @Test
    void getNoticeApi_nonexistentNoticeId() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1234"));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("N001"))
                .andDo(print());
    }

    @Order(6)
    @DisplayName("공지사항 수정 API 성공")
    @WithMockUser(roles = "ADMIN")
    @Test
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
                .principal(new UsernamePasswordAuthenticationToken(admin, null))
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Order(7)
    @DisplayName("공지사항 수정 API 실패 - 유효하지 않은 요청 값")
    @WithMockUser(roles = "ADMIN")
    @Test
    void updateNoticeApi_invalidRequestBody() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목")
                .build();

        // when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/3")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .principal(new UsernamePasswordAuthenticationToken(admin, null))
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("N101"))
                .andDo(print());
    }

    @Order(8)
    @DisplayName("공지사항 수정 API 실패 - 존재하지 않는 ID")
    @WithMockUser(roles = "ADMIN")
    @Test
    void updateNoticeApi_nonexistentId() throws Exception {
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 수정 제목")
                .content("공지사항 수정 내용")
                .build();

        // when
        ResultActions result = mvc.perform(put(PREFIX_URI + "/13")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .principal(new UsernamePasswordAuthenticationToken(admin, null))
                .content(objectMapper.writeValueAsString(noticeRequestDto)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("N001"))
                .andDo(print());
    }

    @Order(9)
    @DisplayName("공지사항 삭제 API 성공")
    @WithMockUser(roles = "ADMIN")
    @Test
    void deleteNoticeApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1")
                .principal(new UsernamePasswordAuthenticationToken(admin, null)));

        // then
        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Order(10)
    @DisplayName("공지사항 삭제 API 실패 - 존재하지 않는 ID")
    @WithMockUser(roles = "ADMIN")
    @Test
    void deleteNoticeApi_nonexistentId() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/1")
                .principal(new UsernamePasswordAuthenticationToken(admin, null)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("N001"))
                .andDo(print());
    }

    @Order(11)
    @DisplayName("공지사항 삭제 API 실패 - 권한 없는 사용자")
    @WithMockUser
    @Test
    void deleteNoticeApi_unAuthorizedUser() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(delete(PREFIX_URI + "/2")
                .principal(new UsernamePasswordAuthenticationToken(user, null)));

        // then
        result.andExpect(status().isForbidden())
                .andDo(print());
    }
}
