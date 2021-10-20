package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes")
public class Likes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Builder
    public Likes(Long id, User user, Portfolio portfolio) {
        this.id = id;
        this.user = user;
        this.portfolio = portfolio;
    }
}
