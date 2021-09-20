package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDto {

    @NotNull(message = "NULL_USER_ID")
    @Pattern(regexp = "^[a-z0-9]*$", message = "INVALID_USER_ID")
    private String username;

    @NotNull(message = "NULL_USER_NAME")
    @Size(min = 2, max = 8, message = "INVALID_USER_NAME")
    private String name;

    @NotNull(message = "NULL_USER_PASSWORD")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}", message = "INVALID_USER_PASSWORD")
    private String password;

    private String college;

    private boolean graduation;

    @NotNull(message = "NULL_USER_WANTEDJOB")
    private String wantedJob;

    @NotNull(message = "NULL_USER_BIRTHDATE")
    @Pattern(regexp="^[0-9]{8}", message="INVALID_SIZE_USER_BIRTHDATE")
    private String birthDate;

    @Builder
    public SignUpRequestDto(String username, String name, String password, String college, boolean graduation, String wantedJob, String birthDate) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.college = college;
        this.graduation = graduation;
        this.wantedJob = wantedJob;
        this.birthDate = birthDate;
    }
}
