package com.linkerbell.portradebackend.domain.admin.service;

import com.linkerbell.portradebackend.domain.admin.domain.Notice;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeDetailResponseDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeListResponseDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeRequestDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeResponseDto;
import com.linkerbell.portradebackend.domain.admin.repository.NoticeRepository;
import com.linkerbell.portradebackend.domain.user.domain.Role;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;
    @Mock
    private NoticeRepository noticeRepository;

    private User admin;
    private User user;

    @BeforeEach
    public void setUp() {
        admin = User.builder()
                .username("admin1")
                .password("1234Aa@@")
                .name("관리자1")
                .birthDate("20030327")
                .wantedJob("marketing")
                .build();
        admin.addRole(Role.ROLE_ADMIN);

        user = User.builder()
                .username("admin1")
                .password("1234Aa@@")
                .name("회원1")
                .birthDate("20030327")
                .wantedJob("marketing")
                .build();
    }

    @DisplayName("Notice 생성")
    @Test
    void createNotice() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목1")
                .content("공지사항 내용1")
                .build();

        // when
        NoticeResponseDto savedNoticeResponseDto = noticeService.createNotice(noticeRequestDto, admin);

        // then
        assertEquals(admin.getUsername(), savedNoticeResponseDto.getCreator());
        assertEquals("공지사항 제목1", savedNoticeResponseDto.getTitle());
        assertEquals(0, savedNoticeResponseDto.getViewCount());
    }

    @DisplayName("Notice 생성 - 권한 없는 사용자")
    @Test
    void createNotice_unAuthorizedUser() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목1")
                .content("공지사항 내용1")
                .build();

        // when
        // then
        assertThrows(UnAuthorizedException.class,
                () -> noticeService.createNotice(noticeRequestDto, user));
    }

    //    TODO
    @DisplayName("Notice 목록 조회")
    @Test
    void getNoticeList() throws Exception {
        // given
//        Page<Notice> noticePage = Page.empty();
        Page<Notice> noticePage = Page.empty();


        given(noticeRepository.findAll(any(Pageable.class)))
                .willReturn(noticePage);

        // when
        NoticeListResponseDto foundNoticeListResponseDto = noticeService.getNoticeList(1, 3);

        // then
//        assertEquals(foundNoticeListResponseDto.);
    }

    @DisplayName("Notice 상세 조회")
    @Test
    void getNotice() throws Exception {
        // given
        Notice notice = Notice.builder()
                .id(1L)
                .user(admin)
                .title("공지사항 제목1")
                .content("공지사항 내용1")
                .viewCount(10)
                .build();

        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(notice));

        // when
        NoticeDetailResponseDto foundNoticeDetailResponseDto = noticeService.getNotice(1L);

        // then
        assertNotNull(foundNoticeDetailResponseDto);
        assertEquals(notice.getUser().getUsername(), foundNoticeDetailResponseDto.getCreator());
        assertEquals(notice.getTitle(), foundNoticeDetailResponseDto.getTitle());
        assertEquals(notice.getContent(), foundNoticeDetailResponseDto.getContent());
        assertEquals(notice.getViewCount(), foundNoticeDetailResponseDto.getViewCount());
        assertEquals(notice.getCreatedDate(), foundNoticeDetailResponseDto.getCreatedDate());
        assertEquals(notice.getLastModifiedDate(), foundNoticeDetailResponseDto.getLastModifiedDate());
    }

    @DisplayName("Notice 상세 조회 - 존재하지 않는 id")
    @Test
    void getNotice_invalidNoticeId() throws Exception {
        // given
        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThrows(IllegalArgumentException.class,
                () -> noticeService.getNotice(1L));
    }

    @DisplayName("Notice 상세 조회 - 조회수 증가")
    @Test
    void getNotice_addViewCount() throws Exception {
        // given
        int beforeViewCount = 10;

        Notice notice = Notice.builder()
                .user(admin)
                .title("공지사항 제목1")
                .content("공지사항 내용1")
                .viewCount(beforeViewCount)
                .build();

        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(notice));

        // when
        NoticeDetailResponseDto foundNoticeDetailResponseDto = noticeService.getNotice(1L);

        // then
        assertNotNull(foundNoticeDetailResponseDto);
        assertEquals(beforeViewCount + 1, foundNoticeDetailResponseDto.getViewCount());
    }
}