package com.linkerbell.portradebackend.domain.user.dto;

import lombok.Getter;

@Getter
public class ExistIdResponseDto {
    private final String userId;

    public ExistIdResponseDto(String userId) {
        this.userId = userId;
    }
}
