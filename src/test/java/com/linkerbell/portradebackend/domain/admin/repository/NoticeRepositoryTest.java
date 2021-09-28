package com.linkerbell.portradebackend.domain.admin.repository;

import com.linkerbell.portradebackend.domain.admin.domain.Notice;
import com.linkerbell.portradebackend.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    private Notice notice;

    @BeforeEach
    public void setUp() {
        User admin = User.builder()
                .username("admin1")
                .password("1234Aa@@")
                .name("관리자1")
                .birthDate("20030327")
                .wantedJob("marketing")
                .build();
        notice = Notice.builder()
                .user(admin)
                .title("공지사항 제목1")
                .content("공지사항 내용1")
                .build();
    }

    @DisplayName("공지사항 엔티티 저장")
    @Test
    void saveNotice() throws Exception {
        // given
        // when
        Notice savedNotice = noticeRepository.save(notice);

        // then
        assertEquals(notice.getId(), savedNotice.getId());
        assertEquals(notice.getTitle(), savedNotice.getTitle());
        assertEquals(notice.getContent(), savedNotice.getContent());
        assertEquals(notice.getUser().getUsername(), savedNotice.getUser().getUsername());
        assertEquals(notice.getViewCount(), savedNotice.getViewCount());
        assertEquals(notice.getCreatedDate(), savedNotice.getCreatedDate());
    }

    @DisplayName("공지사항 엔티티 조회")
    @Test
    void findNoticeById() throws Exception {
        // given
        noticeRepository.save(notice);

        // when
        Notice foundNotice = noticeRepository.findById(notice.getId()).get();

        // then
        assertNotNull(foundNotice);
        assertEquals(notice.getId(), foundNotice.getId());
        assertEquals(notice.getTitle(), foundNotice.getTitle());
        assertEquals(notice.getContent(), foundNotice.getContent());
        assertEquals(notice.getUser().getUsername(), foundNotice.getUser().getUsername());
        assertEquals(notice.getViewCount(), foundNotice.getViewCount());
        assertEquals(notice.getCreatedDate(), foundNotice.getCreatedDate());
    }

    @DisplayName("공지사항 엔티티 삭제")
    @Test
    void deleteNotice() throws Exception {
        // given
        Notice savedNotice = noticeRepository.save(notice);

        // when
        noticeRepository.delete(savedNotice);

        // then
        Optional<Notice> foundNotice = noticeRepository.findById(savedNotice.getId());
        assertEquals(Optional.empty(), foundNotice);
    }
}