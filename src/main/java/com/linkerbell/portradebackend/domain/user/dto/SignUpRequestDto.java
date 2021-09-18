package com.linkerbell.portradebackend.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDto {

    @NotNull(message = "NULL_USER_ID")
    @Pattern(regexp = "/^[a-z0-9]+$/", message = "INVALID_USER_ID")
    private String id;

    @NotNull(message = "NULL_USER_NAME")
    @Size(min = 2, max = 8, message = "INVALID_USER_NAME")
    private String name;

    @NotNull(message = "NULL_USER_PASSWORD")
    @Size(min = 8, max = 20, message = "INVALID_SIZE_USER_PASSWORD")
    //@Pattern(regexp="(?=.[0-9])(?=.[a-zA-Z])(?=.*\\W)(?=\\S+$)", message = "INVALID_USER_PASSWORD")
    private String password;

    private String college;

    private boolean isGraduated;

    @NotNull(message = "NULL_USER_WANTEDJOB")
    private String wantedJob;

    @NotNull(message = "NULL_USER_BIRTHDATE")
    private LocalDate birthDate;

    @Builder
    public SignUpRequestDto(String id, String name, String password, String college, boolean isGraduated, String wantedJob, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.college = college;
        this.isGraduated = isGraduated;
        this.wantedJob = wantedJob;
        this.birthDate = birthDate;
    }
}
