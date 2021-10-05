package com.linkerbell.portradebackend.domain.notice.repository;

import com.linkerbell.portradebackend.domain.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findTopByIdIsGreaterThanOrderByIdAsc(Long id);

    Optional<Notice> findTopByIdIsLessThanOrderByIdDesc(Long id);
}
