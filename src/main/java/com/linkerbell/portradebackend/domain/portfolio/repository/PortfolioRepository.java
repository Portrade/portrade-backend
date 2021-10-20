package com.linkerbell.portradebackend.domain.portfolio.repository;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
