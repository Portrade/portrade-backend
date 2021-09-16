package com.linkerbell.portradebackend.domain.user.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private String id;
    private String name;

    @Builder
    public UserDto(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
