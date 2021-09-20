package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImageResponseDto {

    private String fileName;
    private String url;

    @Builder
    public ProfileImageResponseDto(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
