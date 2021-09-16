package com.linkerbell.portradebackend.domain.admin.domain;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(of = {"id", "title"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "faq")
public class Faq extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @Builder
    public Faq(Long id, User user, String title) {
        this.id = id;
        this.user = user;
        this.title = title;
    }
}
