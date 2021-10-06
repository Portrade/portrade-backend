package com.linkerbell.portradebackend.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpResponseDto {

    private final String userId;
    private final String name;

    @Builder
    public SignUpResponseDto(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
