package com.linkerbell.portradebackend.domain.portfolio.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString(of = {"id", "url"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "portfolio_image")
public class PortfolioImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "porfolio_id")
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
