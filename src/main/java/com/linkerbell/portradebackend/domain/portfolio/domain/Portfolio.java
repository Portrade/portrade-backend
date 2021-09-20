package com.linkerbell.portradebackend.domain.portfolio.domain;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(exclude = {"user", "lastModifiedDate"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "portfolio")
public class Portfolio extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @Builder
    public Portfolio(Long id, User user, String title, String description, boolean isPublic, int viewCount, String mainImage) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.viewCount = viewCount;
        this.mainImage = mainImage;
    }
}
