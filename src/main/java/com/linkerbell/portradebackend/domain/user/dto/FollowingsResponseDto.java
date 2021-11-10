package com.linkerbell.portradebackend.domain.user.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FollowingsResponseDto {

    private final PageResponseDto page;
    private final List<ProfileResponseDto> followings;

    @Builder
    public FollowingsResponseDto(PageResponseDto page, List<ProfileResponseDto> followings) {
        this.page = page;
        this.followings = followings;
    }
}
