package com.linkerbell.portradebackend.domain.portfolio.domain;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = {"user", "portfolio"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "portfolio_save")
public class PortfolioSave extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_save_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Builder
    public PortfolioSave(Long id, User user, Portfolio portfolio) {
        this.id = id;
        this.user = user;
        this.portfolio = portfolio;
    }
}
