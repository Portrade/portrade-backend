package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileRequestDto {

    @NotNull(message = "NULL_USER_NAME")
    @Size(min = 2, max = 8, message = "INVALID_USER_NAME")
    private String name;

    @NotNull(message = "NULL_USER_BIRTHDATE")
    @Size(min = 8, max = 8, message = "INVALID_SIZE_USER_BIRTHDATE")
    private String birthDate;

    @NotNull(message = "NULL_USER_WANTEDJOB")
    private String wantedJob;

    private String college;

    private boolean isGraduated;

    @Builder
    public ProfileRequestDto(String name, String birthDate, String wantedJob, String college, boolean isGraduated) {
        this.name = name;
        this.birthDate = birthDate;
        this.wantedJob = wantedJob;
        this.college = college;
        this.isGraduated = isGraduated;
    }
}
