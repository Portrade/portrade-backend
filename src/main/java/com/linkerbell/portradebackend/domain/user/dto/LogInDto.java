package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogInDto {

    @NotNull
    private String email;
    @NotNull
    private String password;

    @Builder
    public LogInDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
