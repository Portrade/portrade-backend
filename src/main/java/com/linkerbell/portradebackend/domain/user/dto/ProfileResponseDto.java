package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private final String id;
    private final String name;
    private final String profileImageUrl;
    private final String job;

    @Builder
    public ProfileResponseDto(String id, String name, String profileImageUrl, String job) {
        this.id = id;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.job = job;
    }
}
