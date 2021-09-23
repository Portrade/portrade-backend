package com.linkerbell.portradebackend.domain.admin.domain;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(exclude = {"user"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna")
public class Qna extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    //문의 분류
    @Enumerated(EnumType.STRING)
    private Category category;

    //답변 완료 유무
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @Builder
    public Qna(Long id, User user, String name, String email, String phoneNumber, String title, String content, Category category, Status status, boolean isPublic) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.title = title;
        this.content = content;
        this.category = category;
        this.status = status;
        this.isPublic = isPublic;
    }
}
