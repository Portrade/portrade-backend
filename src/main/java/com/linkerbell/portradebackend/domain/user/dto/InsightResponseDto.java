package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InsightResponseDto {

    private final int viewCount;
    private final int likeCount;
    private final int commentCount;
    private final int followingCount;
    private final int followerCount;

    @Builder
    public InsightResponseDto(int viewCount, int likeCount, int commentCount, int followingCount, int followerCount) {
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }
}
