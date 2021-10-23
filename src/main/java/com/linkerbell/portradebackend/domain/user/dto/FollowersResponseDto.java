package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FollowersResponseDto {
    private final List<ProfileResponeDto> followers;
    private final int maxPage;

    @Builder
    public FollowersResponseDto(List<ProfileResponeDto> followers, int maxPage) {
        this.followers = followers;
        this.maxPage = maxPage;
    }
}
