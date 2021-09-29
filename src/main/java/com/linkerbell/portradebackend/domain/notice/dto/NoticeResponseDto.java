package com.linkerbell.portradebackend.domain.notice.dto;

import com.linkerbell.portradebackend.domain.notice.domain.Notice;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeResponseDto {

    private Long id;
    private String creator;
    private String title;
    private int viewCount;
    private LocalDateTime createdDate;

    @Builder
    public NoticeResponseDto(Long id, String creator, String title, int viewCount, LocalDateTime createdDate) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
    }

    public static NoticeResponseDto of(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .creator(notice.getUser().getUsername())
                .title(notice.getTitle())
                .viewCount(notice.getViewCount())
                .createdDate(notice.getCreatedDate())
                .build();
    }
}
