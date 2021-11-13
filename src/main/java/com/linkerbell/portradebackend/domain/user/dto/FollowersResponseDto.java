package com.linkerbell.portradebackend.domain.user.dto;

import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FollowersResponseDto {

    private final PageResponseDto page;
    private final List<ProfileResponseDto> followers;

    @Builder
    public FollowersResponseDto(PageResponseDto page, List<ProfileResponseDto> followers) {
        this.page = page;
        this.followers = followers;
    }
}
