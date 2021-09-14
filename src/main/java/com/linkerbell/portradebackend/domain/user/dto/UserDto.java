package com.linkerbell.portradebackend.domain.user.dto;


import com.linkerbell.portradebackend.domain.user.domain.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private UUID id;
    private String email;
    private String name;
    private Set<Role> roles;
    private String college;
    private String jobStatus;
    private int birthDate;

    @Builder
    public UserDto(UUID id, String email, String name, Set<Role> roles, String college, String jobStatus, int birthDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.roles = roles;
        this.college = college;
        this.jobStatus = jobStatus;
        this.birthDate = birthDate;
    }
}
