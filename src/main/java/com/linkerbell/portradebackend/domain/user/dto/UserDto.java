package com.linkerbell.portradebackend.domain.user.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private String username;
    private String password;

    @Builder
    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
