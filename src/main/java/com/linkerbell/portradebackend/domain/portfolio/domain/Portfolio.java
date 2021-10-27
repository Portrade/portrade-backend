package com.linkerbell.portradebackend.domain.portfolio.domain;

import com.linkerbell.portradebackend.domain.comment.domain.Comment;
import com.linkerbell.portradebackend.domain.file.domain.PortfolioContentFile;
import com.linkerbell.portradebackend.domain.file.domain.PortfolioMainImage;
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
@ToString(exclude = {"creator", "mainImage", "contentFiles", "likes", "comments"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "portfolio")
public class Portfolio extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

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

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @OneToOne(mappedBy = "portfolio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PortfolioMainImage mainImage;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<PortfolioContentFile> contentFiles = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "portfolio")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Portfolio(Long id, User creator, String title, String description, String category, boolean isPublic, int viewCount, LocalDateTime lastModifiedDate, PortfolioMainImage mainImage, List<PortfolioContentFile> contentFiles, List<Likes> likes, List<Comment> comments) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isPublic = isPublic;
        this.viewCount = viewCount;
        this.lastModifiedDate = lastModifiedDate;
        this.mainImage = mainImage;
        this.contentFiles = contentFiles;
        this.likes = likes;
        this.comments = comments;
    }

    public void addViewCount() {
        viewCount++;
    }

    public int getLikeCount() {
        return likes.size();
    }

    public int getCommentCount() {
        return comments.size();
    }
}
