package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImageResponseDto {

    @NotNull
    private String fileName;

    @NotNull
    private String url;

    @Builder
    public ProfileImageResponseDto(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
