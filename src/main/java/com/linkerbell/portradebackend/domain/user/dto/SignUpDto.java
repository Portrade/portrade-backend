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
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String password;

    private String college;

    private boolean isGraduated;

    @NotNull
    private String wantedJob;

    @NotNull
    private int birthDate;

    @Builder
    public SignUpDto(String id, String name, String password, String college, boolean isGraduated, String wantedJob, int birthDate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.college = college;
        this.isGraduated = isGraduated;
        this.wantedJob = wantedJob;
        this.birthDate = birthDate;
    }
}
