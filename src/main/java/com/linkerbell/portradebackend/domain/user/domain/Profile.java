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

    @Column(name = "profile_url")
    private String profileUrl;

    @Builder
    public Profile(String college, boolean isGraduated, String profileUrl) {
        this.college = college;
        this.isGraduated = isGraduated;
        this.profileUrl = profileUrl;
    }

    public void updateProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
