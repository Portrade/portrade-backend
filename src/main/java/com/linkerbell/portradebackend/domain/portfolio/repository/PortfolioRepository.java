package com.linkerbell.portradebackend.domain.portfolio.repository;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query("select p from Portfolio p join fetch p.user u where u.username = :userId")
    List<Portfolio> findAllByUsername(@Param("userId") String userId);

    @Query("select p from Portfolio p join p.user u where u.username = :userId")
    Page<Portfolio> findAllByUsername(Pageable pageable, @Param("userId") String userId);
}
