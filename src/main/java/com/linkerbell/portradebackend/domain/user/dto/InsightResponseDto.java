package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InsightResponseDto {
    private final int viewCount;
    private final int likes;
    private final int comment;
    private final long followers;
    private final long followings;

    @Builder
    public InsightResponseDto(int viewCount, int likes, int comment, long followers, long followings) {
        this.viewCount = viewCount;
        this.likes = likes;
        this.comment = comment;
        this.followers = followers;
        this.followings = followings;
    }
}
