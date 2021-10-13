package com.linkerbell.portradebackend.domain.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private String college;

    @Column(name = "is_graduated")
    private boolean isGraduated;

    @Column(name = "profile_url")
    private String profileUrl;

    private String job;

    @Builder
    public Profile(String college, boolean isGraduated, String profileUrl, String job) {
        this.college = college;
        this.isGraduated = isGraduated;
        this.profileUrl = profileUrl;
        this.job = job;
    }

    public void updateProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
