package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Getter;

@Getter
public class TokenResponseDto {

    private final String accessToken;

    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}