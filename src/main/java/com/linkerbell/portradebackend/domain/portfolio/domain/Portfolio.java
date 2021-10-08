package com.linkerbell.portradebackend.domain.portfolio.domain;

import com.linkerbell.portradebackend.domain.comment.domain.Comment;
import com.linkerbell.portradebackend.domain.user.domain.Likes;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(exclude = {"user"})
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

    @Column(nullable = false)
    private String category;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @OneToMany(mappedBy = "portfolio")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Portfolio(Long id, User user, String title, String description, String category, boolean isPublic, int viewCount, String mainImage) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isPublic = isPublic;
        this.viewCount = viewCount;
        this.mainImage = mainImage;
    }
}
