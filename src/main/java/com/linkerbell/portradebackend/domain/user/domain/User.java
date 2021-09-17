package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.domain.follow.domain.Follow;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@ToString(of = {"id", "username", "name", "college", "birthDate"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String college;

    @Column(name = "is_graduated")
    private boolean isGraduated;

    @Column(name = "wanted_job")
    private String wantedJob;

    @Column(name = "job_status")
    private String jobStatus;

    @Column(name = "birth_date", nullable = false)
    private int birthDate;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "followUser")
    private List<Follow> follows = new ArrayList<>();

    @Builder
    public User(UUID id, String username, String password, String name, String college, boolean isGraduated, String wantedJob, String jobStatus, int birthDate, String profileUrl) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.college = college;
        this.isGraduated = isGraduated;
        this.wantedJob = wantedJob;
        this.jobStatus = jobStatus;
        this.birthDate = birthDate;
        this.profileUrl = profileUrl;

        this.roles.add(Role.ROLE_USER);
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void deleteRole(Role role) {
        roles.remove(role);
    }
}
