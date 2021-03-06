package com.linkerbell.portradebackend.domain.notice.domain;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(exclude = {"user"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notice")
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Builder
    public Notice(Long id, User user, String title, String content, int viewCount) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        lastModifiedDate = LocalDateTime.now();
    }

    public void addViewCount() {
        viewCount += 1;
    }
}
