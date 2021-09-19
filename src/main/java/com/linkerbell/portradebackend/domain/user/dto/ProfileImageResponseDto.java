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
    private String name;

    @NotNull
    private String url;

    @Builder
    public ProfileImageResponseDto(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
