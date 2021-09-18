package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_portfolio")
public class UserPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_portfolio_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Builder
    public UserPortfolio(Long id, User user, Portfolio portfolio) {
        this.id = id;
        this.user = user;
        this.portfolio = portfolio;
    }
}
