package com.linkerbell.portradebackend.domain.comment.repository;

import com.linkerbell.portradebackend.domain.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
//    TODO
//    int countByPortfolio_Id(Long id);
//
//    List<Comment> findTop10ByPortfolio_IdAndIdLessThan(Long portfolioId, Long lastCommentId);
//
//    boolean existsByPortfolio_IdAndIdLessThan(Long portfolioId, Long lastCommentId);
//
//    List<Comment> findTop10ByPortfolio_Id(Long portfolioId);

    Page<Comment> findAllByPortfolio_Id(Pageable pageable, Long portfolioId);
}
