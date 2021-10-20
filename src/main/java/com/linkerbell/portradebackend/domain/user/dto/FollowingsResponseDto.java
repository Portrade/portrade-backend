package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class FollowingsResponseDto {
    private final List<ProfileResponeDto> followings;
    private final int maxPage;

    @Builder
    public FollowingsResponseDto(List<ProfileResponeDto> followings, int maxPage) {
        this.followings = followings;
        this.maxPage = maxPage;
    }
}
