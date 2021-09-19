package com.linkerbell.portradebackend.domain.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private String college;

    @Column(name = "is_graduated")
    private boolean isGraduated;

    @Builder
    public Profile(String college, boolean isGraduated) {
        this.college = college;
        this.isGraduated = isGraduated;
    }
}
