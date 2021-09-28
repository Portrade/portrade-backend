package com.linkerbell.portradebackend.domain.portfolio.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString(exclude = {"portfolio"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "portfolio_image")
public class PortfolioImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Column(nullable = false)
    private String url;

    @Builder
    public PortfolioImage(Long id, Portfolio portfolio, String url) {
        this.id = id;
        this.portfolio = portfolio;
        this.url = url;
    }
}
