package com.linkerbell.portradebackend.domain.user.dto;

import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobRequestDto {

    @NotNull(message = "NULL_USER_JOB")
    private String job;

    public JobRequestDto(String job) {
        this.job = job;
    }
}
