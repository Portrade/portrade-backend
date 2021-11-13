package com.linkerbell.portradebackend.global.common.dto;

import lombok.Getter;

@Getter
public class IdResponseDto {

    private final Long id;

    public IdResponseDto(Long id) {
        this.id = id;
    }
}
