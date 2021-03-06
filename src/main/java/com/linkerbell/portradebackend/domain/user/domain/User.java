package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@ToString(exclude = {"password"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private String birthDate;

    @Column(name = "wanted_job", nullable = false)
    private String wantedJob;

    @Embedded
    private Profile profile;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Builder
    public User(UUID id, String username, String password, String name, String birthDate, String wantedJob, Profile profile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.wantedJob = wantedJob;
        this.profile = profile;
        this.roles.add(Role.ROLE_USER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void deleteRole(Role role) {
        roles.remove(role);
    }

    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }

    public String getUserProfileUrl() {
        return profile.getProfileImageFile().getUrl();
    }

    public String getUserJob() {
        return profile.getJob();
    }

    public void updateProfile(String name, String birthDate, String wantedJob, String college, boolean isGraduated) {
        this.name = name;
        this.birthDate = birthDate;
        this.wantedJob = wantedJob;
        profile.updateProfile(college, isGraduated);
    }

    public void updateJob(String job) {
        profile.updateProfileJob(job);
    }
}
