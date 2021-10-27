package com.linkerbell.portradebackend.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpResponseDto {

    private final String id;
    private final String name;

    @Builder
    public SignUpResponseDto(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
