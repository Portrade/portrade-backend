package com.linkerbell.portradebackend.domain.user.repository;

import com.linkerbell.portradebackend.domain.user.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Integer countByPortfolioId(Long id);
}
