package com.linkerbell.portradebackend.domain.save.repository;

import com.linkerbell.portradebackend.domain.save.domain.UserPortfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, Long> {

    @Query(value = "select u from UserPortfolio u join fetch u.portfolio p where u.user.id = :id",
            countQuery = " select count(u) from UserPortfolio u join u.user us where us.id = :id")
    Page<UserPortfolio> findByUser_Id(Pageable pageable, @Param("id") UUID id);
}
