package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponseDto {

    @NotNull
    private String accessToken;

    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
