package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.global.common.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private String college;

    @Column(name = "is_graduated")
    private boolean isGraduated;

    @Embedded
    private File profileImageFile;

    private String job;

    @Builder
    public Profile(String college, boolean isGraduated, File profileImageFile, String job) {
        this.college = college;
        this.isGraduated = isGraduated;
        this.profileImageFile = profileImageFile;
        this.job = job;
    }

    public void updateProfileJob(String job) {
        this.job = job;
    }

    public void updateProfile(String college, boolean isGraduated) {
        this.college = college;
        this.isGraduated = isGraduated;
    }

    public void updateProfileImageFile(File profileImageFile) {
        this.profileImageFile = profileImageFile;
    }
}
