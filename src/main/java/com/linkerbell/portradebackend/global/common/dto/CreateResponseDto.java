package com.linkerbell.portradebackend.global.common.dto;

import lombok.Getter;

@Getter
public class CreateResponseDto {

    private Long id;

    public CreateResponseDto(Long id) {
        this.id = id;
    }
}
