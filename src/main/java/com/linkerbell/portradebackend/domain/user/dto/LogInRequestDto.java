package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogInRequestDto {

    @NotNull
    private String id;

    @NotNull
    private String password;

    @Builder
    public LogInRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
