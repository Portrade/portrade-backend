package com.linkerbell.portradebackend.domain.comment.repository;

import com.linkerbell.portradebackend.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Integer countByPortfolioId(Long id);
}
