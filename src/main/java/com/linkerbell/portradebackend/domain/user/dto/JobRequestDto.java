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

    public User toEntity(User user) {
        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .birthDate(user.getBirthDate())
                .wantedJob(user.getWantedJob())
                .profile(Profile.builder()
                        .college(user.getUserCollege())
                        .isGraduated(user.isUserGraduated())
                        .profileUrl(user.getUserProfileUrl())
                        .job(job)
                        .build())
                .build();
    }
}
