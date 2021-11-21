package com.linkerbell.portradebackend.domain.portfolio.repository;

import com.linkerbell.portradebackend.domain.portfolio.domain.PortfolioSave;
import com.linkerbell.portradebackend.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioSaveRepository extends JpaRepository<PortfolioSave, Long> {

    Optional<PortfolioSave> findByPortfolio_IdAndUser_Username(Long portfolioId, String userId);

    Page<PortfolioSave> findAllByUser(Pageable pageable, User user);
}
