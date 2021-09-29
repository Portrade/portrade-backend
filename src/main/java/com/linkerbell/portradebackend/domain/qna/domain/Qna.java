package com.linkerbell.portradebackend.domain.qna.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NotExsitException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@ToString(exclude = {"user"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Qna extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    protected Qna(Long id, User user, String title, String content, boolean isPublic) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
    }

    public UUID Id() {
        if(Objects.isNull(user)) {
            throw new NotExsitException(ErrorCode.NONEXISTENT_USER);
        }
        return user.getId();
    }
}
