package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogInRequestDto {

    @NotNull(message = "NULL_USER_ID")
    private String id;

    @NotNull(message = "NULL_USER_PASSWORD")
    private String password;

    @Builder
    public LogInRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
