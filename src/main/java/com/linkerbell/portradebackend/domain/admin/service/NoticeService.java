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
    public NoticeResponseDto createNotice(NoticeRequestDto noticeRequestDto, User user) {
        if (!user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new UnAuthorizedException("권한이 없습니다.");
        }

        Notice notice = Notice.builder()
                .user(user)
                .title(noticeRequestDto.getTitle())
                .content(noticeRequestDto.getContent())
                .build();
        noticeRepository.save(notice);

        return NoticeResponseDto.builder()
                .id(notice.getId())
                .creator(notice.getUser().getName())
                .title(notice.getTitle())
                .viewCount(notice.getViewCount())
                .createdDate(notice.getCreatedDate())
                .build();
    }

    public NoticeListResponseDto getNoticeList(int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "created_date"));
        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        List<NoticeResponseDto> notices = noticePage.stream()
                .map(notice -> NoticeResponseDto.builder()
                        .id(notice.getId())
                        .creator(notice.getUser().getName())
                        .title(notice.getTitle())
                        .viewCount(notice.getViewCount())
                        .createdDate(notice.getCreatedDate())
                        .build())
                .collect(Collectors.toList());

        return NoticeListResponseDto.builder()
                .maxPage(noticePage.getTotalPages())
                .notices(notices)
                .build();
    }

    public NoticeDetailResponseDto getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항 번호입니다."));

        notice.addViewCount();

        return NoticeDetailResponseDto.builder()
                .id(notice.getId())
                .creator(notice.getUser().getName()) // 이름? 아이디?
                .title(notice.getTitle())
                .content(notice.getContent())
                .viewCount(notice.getViewCount())
                .createdDate(notice.getCreatedDate())
                .lastModifiedDate(notice.getLastModifiedDate())
                .build();
    }
}
