package com.linkerbell.portradebackend.domain.portfolio.repository;

import com.linkerbell.portradebackend.domain.portfolio.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    int countByPortfolioId(Long portfolioId);

    Optional<Likes> findByPortfolio_IdAndUser_Username(Long portfolioId, String userId);
}
