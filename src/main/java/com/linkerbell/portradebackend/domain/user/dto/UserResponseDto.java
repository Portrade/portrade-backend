package com.linkerbell.portradebackend.domain.user.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponseDto {

    private String userId;
    private String name;

    @Builder
    public UserResponseDto(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
