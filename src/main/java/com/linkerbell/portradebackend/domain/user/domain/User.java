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
@ToString(of = {"id", "email", "name", "college", "birthDate"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    private String college;

    @Column(nullable = false, name = "job_status")
    private String jobStatus;

    @Column(name = "birth_date", nullable = false)
    private int birthDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @OneToMany(mappedBy = "followUser")
    private List<Follow> follows = new ArrayList<>();

    @Builder
    public User(UUID id, String email, String password, String name, String college, String jobStatus, int birthDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.college = college;
        this.jobStatus = jobStatus;
        this.birthDate = birthDate;

        this.roles.add(Role.ROLE_USER);
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void deleteRole(Role role) {
        roles.remove(role);
    }
}
