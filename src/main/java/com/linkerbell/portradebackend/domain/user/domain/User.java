package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.domain.follow.domain.Follow;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    private String college;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @OneToMany(mappedBy = "followUser")
    private List<Follow> follows = new ArrayList<>();

    @Builder
    public User(UUID id, String email, String password, String name, String college, LocalDate birthDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.college = college;
        this.birthDate = birthDate;
    }
}
