package com.linkerbell.portradebackend.domain.notice.service;

import com.linkerbell.portradebackend.domain.notice.domain.Notice;
import com.linkerbell.portradebackend.domain.notice.dto.NoticeDetailResponseDto;
import com.linkerbell.portradebackend.domain.notice.dto.NoticeRequestDto;
import com.linkerbell.portradebackend.domain.notice.dto.NoticesResponseDto;
import com.linkerbell.portradebackend.domain.notice.repository.NoticeRepository;
import com.linkerbell.portradebackend.domain.user.domain.Role;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.exception.custom.NotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;
    @Mock
    private NoticeRepository noticeRepository;

    private User admin;

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
    }

    @DisplayName("공지사항 생성 성공")
    @Test
    void createNotice() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 제목1")
                .content("공지사항 내용1")
                .build();

        // when
        noticeService.createNotice(noticeRequestDto, admin);

        // then
        verify(noticeRepository, times(1))
                .save(any(Notice.class));
    }

    @DisplayName("공지사항 목록 조회 성공")
    @Test
    void getNoticeList() throws Exception {
        // given
        Notice notice1 = Notice.builder()
                .title("공지사항 제목1")
                .content("공지사항 내용1")
                .user(admin)
                .build();
        Notice notice2 = Notice.builder()
                .title("공지사항 제목2")
                .content("공지사항 내용2")
                .user(admin)
                .build();
        Notice notice3 = Notice.builder()
                .title("공지사항 제목3")
                .content("공지사항 내용3")
                .user(admin)
                .build();
        List<Notice> notices = new ArrayList<>(Arrays.asList(notice1, notice2, notice3));
        Page<Notice> noticePage = new PageImpl<>(notices);

        given(noticeRepository.findAll(any(Pageable.class)))
                .willReturn(noticePage);

        // when
        NoticesResponseDto foundNoticesResponseDto = noticeService.getNotices(1, 3, "");

        // then
        assertEquals(foundNoticesResponseDto.getMaxPage(), 1);
        assertEquals(foundNoticesResponseDto.getNotices().size(), 3);
    }

    @DisplayName("공지사항 상세 조회 성공")
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
        assertNotNull(notice);
        assertNotNull(foundNoticeDetailResponseDto);
        assertEquals(notice.getUser().getUsername(), foundNoticeDetailResponseDto.getCreator());
        assertEquals(notice.getTitle(), foundNoticeDetailResponseDto.getTitle());
        assertEquals(notice.getContent(), foundNoticeDetailResponseDto.getContent());
        assertEquals(notice.getViewCount(), foundNoticeDetailResponseDto.getViewCount());
        assertEquals(notice.getCreatedDate(), foundNoticeDetailResponseDto.getCreatedDate());
        assertEquals(notice.getLastModifiedDate(), foundNoticeDetailResponseDto.getLastModifiedDate());
    }

    @DisplayName("공지사항 상세 조회 실패 - 존재하지 않는 ID")
    @Test
    void getNotice_nonexistentId() throws Exception {
        // given
        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThrows(NotExistException.class,
                () -> noticeService.getNotice(1L));
    }

    @DisplayName("공지사항 상세 조회 성공 - 조회수 증가 확인")
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

    @DisplayName("공지사항 수정 성공")
    @Test
    void updateNotice() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 수정 제목")
                .content("공지사항 수정 내용")
                .build();
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
        noticeService.updateNotice(1L, noticeRequestDto);

        // then
        assertNotNull(notice);
        assertEquals("공지사항 수정 제목", notice.getTitle());
        assertEquals("공지사항 수정 내용", notice.getContent());
    }

    @DisplayName("공지사항 수정 실패 - 존재하지 않는 ID")
    @Test
    void updateNotice_nonexistentId() throws Exception {
        // given
        NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                .title("공지사항 수정 제목")
                .content("공지사항 수정 내용")
                .build();

        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThrows(NotExistException.class,
                () -> noticeService.updateNotice(1L, noticeRequestDto));
    }

    @DisplayName("공지사항 삭제 성공")
    @Test
    void deleteNotice() throws Exception {
        // given
        Notice notice = Notice.builder()
                .user(admin)
                .title("공지사항 제목1")
                .content("공지사항 내용1")
                .viewCount(15)
                .build();

        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(notice));

        // when
        noticeService.deleteNotice(1L);

        // then
        verify(noticeRepository, times(1))
                .delete(any(Notice.class));
    }

    @DisplayName("공지사항 삭제 실패 - 존재하지 않는 ID")
    @Test
    void deleteNotice_nonexistentId() throws Exception {
        // given
        given(noticeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThrows(NotExistException.class,
                () -> noticeService.deleteNotice(1L));
    }
}