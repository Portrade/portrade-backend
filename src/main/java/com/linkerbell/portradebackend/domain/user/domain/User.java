package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.domain.follow.domain.Follow;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private int birthDate;

    @Embedded
    private Profile profile;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @OneToMany(mappedBy = "followUser")
    private List<Follow> follows = new ArrayList<>();

    @Builder
    public User(UUID id, String username, String password, String name, int birthDate, Profile profile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.profile = profile;
    }
}
