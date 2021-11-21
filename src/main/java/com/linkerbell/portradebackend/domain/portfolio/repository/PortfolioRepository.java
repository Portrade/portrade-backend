package com.linkerbell.portradebackend.domain.portfolio.repository;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findAllByCreator_Username(String userId);

    Page<Portfolio> findAllByCreator_Username(Pageable pageable, String userId);

    Page<Portfolio> findAllByCategoryEqualsIgnoreCase(Pageable pageable, String category);

    Page<Portfolio> findAllByTitleContainingIgnoreCaseOrderByIdAsc(Pageable pageable, String keyword);

    Page<Portfolio> findAllByTitleContainingIgnoreCaseOrderByIdDesc(Pageable pageable, String keyword);

    // 검색 - 조회수 정렬
    Page<Portfolio> findAllByTitleContainingIgnoreCaseOrderByViewCountAsc(Pageable pageable, String keyword);

    Page<Portfolio> findAllByTitleContainingIgnoreCaseOrderByViewCountDesc(Pageable pageable, String keyword);

    // 검색 - 사전순 정렬
    Page<Portfolio> findAllByTitleContainingIgnoreCaseOrderByTitleAsc(Pageable pageable, String keyword);

    Page<Portfolio> findAllByTitleContainingIgnoreCaseOrderByTitleDesc(Pageable pageable, String keyword);
}
