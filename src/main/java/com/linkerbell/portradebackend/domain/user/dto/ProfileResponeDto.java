package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ProfileResponeDto {
    private final UUID id;
    private final String name;
    private final String profileUrl;
    private final String job;

    @Builder
    public ProfileResponeDto(UUID id, String name, String profileUrl, String job) {
        this.id = id;
        this.name = name;
        this.profileUrl = profileUrl;
        this.job = job;
    }
}
