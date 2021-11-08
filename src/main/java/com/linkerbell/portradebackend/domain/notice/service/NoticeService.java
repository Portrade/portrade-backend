package com.linkerbell.portradebackend.domain.notice.service;

import com.linkerbell.portradebackend.domain.notice.domain.Notice;
import com.linkerbell.portradebackend.domain.notice.dto.NoticeDetailResponseDto;
import com.linkerbell.portradebackend.domain.notice.dto.NoticeRequestDto;
import com.linkerbell.portradebackend.domain.notice.dto.NoticeResponseDto;
import com.linkerbell.portradebackend.domain.notice.dto.NoticesResponseDto;
import com.linkerbell.portradebackend.domain.notice.repository.NoticeRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public IdResponseDto createNotice(NoticeRequestDto noticeRequestDto, User user) {
        Notice notice = Notice.builder()
                .user(user)
                .title(noticeRequestDto.getTitle())
                .content(noticeRequestDto.getContent())
                .build();
        noticeRepository.save(notice);

        return new IdResponseDto(notice.getId());
    }

    public NoticesResponseDto getNotices(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Notice> noticePage = null;

        if(keyword.equals(""))
            noticePage = noticeRepository.findAll(pageable);
        else
            noticePage = noticeRepository.findAllByTitleContainingAndContentContainingIgnoreCase(pageable, keyword, keyword);

        List<NoticeResponseDto> notices = noticePage.stream()
                .map(NoticeResponseDto::of)
                .collect(Collectors.toList());

        return NoticesResponseDto.builder()
                .maxPage(noticePage.getTotalPages())
                .notices(notices)
                .build();
    }

    @Transactional
    public NoticeDetailResponseDto getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_NOTICE));

        notice.addViewCount();

        Optional<Notice> nextNoticeOptional = noticeRepository.findTopByIdIsGreaterThanOrderByIdAsc(noticeId);
        Optional<Notice> prevNoticeOptional = noticeRepository.findTopByIdIsLessThanOrderByIdDesc(noticeId);

        NoticeResponseDto nextNoticeResponseDto = nextNoticeOptional.isPresent()
                ? NoticeResponseDto.of(nextNoticeOptional.get())
                : null;
        NoticeResponseDto prevNoticeResponseDto = prevNoticeOptional.isPresent()
                ? NoticeResponseDto.of(prevNoticeOptional.get())
                : null;

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
    public IdResponseDto updateNotice(Long noticeId, NoticeRequestDto noticeRequestDto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_NOTICE));

        notice.update(noticeRequestDto.getTitle(), noticeRequestDto.getContent());

        return new IdResponseDto(noticeId);
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_NOTICE));

        noticeRepository.delete(notice);
    }
}
