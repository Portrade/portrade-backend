package com.linkerbell.portradebackend.domain.admin.service;

import com.linkerbell.portradebackend.domain.admin.domain.Notice;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeDetailResponseDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeRequestDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticeResponseDto;
import com.linkerbell.portradebackend.domain.admin.dto.NoticesResponseDto;
import com.linkerbell.portradebackend.domain.admin.repository.NoticeRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public CreateResponseDto createNotice(NoticeRequestDto noticeRequestDto, User user) {
        Notice notice = Notice.builder()
                .user(user)
                .title(noticeRequestDto.getTitle())
                .content(noticeRequestDto.getContent())
                .build();
        noticeRepository.save(notice);

        return new CreateResponseDto(notice.getId());
    }

    public NoticesResponseDto getNotices(int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        List<NoticeResponseDto> notices = noticePage.stream()
                .map(notice -> NoticeResponseDto.builder()
                        .id(notice.getId())
                        .creator(notice.getUser().getUsername())
                        .title(notice.getTitle())
                        .viewCount(notice.getViewCount())
                        .createdDate(notice.getCreatedDate())
                        .build())
                .collect(Collectors.toList());

        return NoticesResponseDto.builder()
                .maxPage(noticePage.getTotalPages())
                .notices(notices)
                .build();
    }

    @Transactional
    public NoticeDetailResponseDto getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new InvalidValueException(ErrorCode.NONEXISTENT_NOTICE_ID));
        Notice nextNotice = noticeRepository.findTopByIdIsGreaterThan(noticeId)
                .orElse(null);
        Notice prevNotice = noticeRepository.findTopByIdIsLessThan(noticeId)
                .orElse(null);

        notice.addViewCount();

        NoticeResponseDto nextNoticeResponseDto = null;
        NoticeResponseDto prevNoticeResponseDto = null;
        if (nextNotice != null) {
            nextNoticeResponseDto = NoticeResponseDto.builder()
                    .id(nextNotice.getId())
                    .creator(nextNotice.getUser().getUsername())
                    .title(nextNotice.getTitle())
                    .viewCount(nextNotice.getViewCount())
                    .createdDate(nextNotice.getCreatedDate())
                    .build();
        }
        if (prevNotice != null) {
            prevNoticeResponseDto = NoticeResponseDto.builder()
                    .id(prevNotice.getId())
                    .creator(prevNotice.getUser().getUsername())
                    .title(prevNotice.getTitle())
                    .viewCount(prevNotice.getViewCount())
                    .createdDate(prevNotice.getCreatedDate())
                    .build();
        }

        return NoticeDetailResponseDto.builder()
                .id(notice.getId())
                .creator(notice.getUser().getUsername())
                .title(notice.getTitle())
                .content(notice.getContent())
                .viewCount(notice.getViewCount())
                .createdDate(notice.getCreatedDate())
                .lastModifiedDate(notice.getLastModifiedDate())
                .next(nextNoticeResponseDto)
                .prev(prevNoticeResponseDto)
                .build();
    }

    @Transactional
    public void updateNotice(Long noticeId, NoticeRequestDto noticeRequestDto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new InvalidValueException(ErrorCode.NONEXISTENT_NOTICE_ID));

        notice.update(noticeRequestDto.getTitle(), noticeRequestDto.getContent());
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new InvalidValueException(ErrorCode.NONEXISTENT_NOTICE_ID));

        noticeRepository.delete(notice);
    }
}
