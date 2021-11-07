package com.linkerbell.portradebackend.domain.comment.domain;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = {"creator", "portfolio"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(Long id, User creator, Portfolio portfolio, String content) {
        this.id = id;
        this.creator = creator;
        this.portfolio = portfolio;
        this.content = content;
    }
}
