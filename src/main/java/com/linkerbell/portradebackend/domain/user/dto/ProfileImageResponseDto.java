package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileImageResponseDto {

    private final String fileName;
    private final String url;

    @Builder
    public ProfileImageResponseDto(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
