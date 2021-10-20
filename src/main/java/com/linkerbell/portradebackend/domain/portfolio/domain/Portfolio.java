package com.linkerbell.portradebackend.domain.portfolio.domain;

import com.linkerbell.portradebackend.domain.file.domain.PortfolioContentFile;
import com.linkerbell.portradebackend.domain.file.domain.PortfolioMainImage;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(exclude = {"creator", "mainImage", "contentFiles"})
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

//    TODO
//    @OneToMany(mappedBy = "portfolio")
//    private List<Likes> likes = new ArrayList<>();

    @Builder
    public Portfolio(Long id, User creator, String title, String description, String category, boolean isPublic, int viewCount, PortfolioMainImage mainImage, List<PortfolioContentFile> contentFiles) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isPublic = isPublic;
        this.viewCount = viewCount;
        this.mainImage = mainImage;
        this.contentFiles = contentFiles;
    }

    public void addViewCount() {
        viewCount++;
    }
}
