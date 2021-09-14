package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpDto {

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    private String college;

    @NotNull
    private String jobStatus;

    @NotNull
    private int birthDate;

    @Builder
    public SignUpDto(String email, String name, String password, String college, String jobStatus, int birthDate) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.college = college;
        this.jobStatus = jobStatus;
        this.birthDate = birthDate;
    }
}
