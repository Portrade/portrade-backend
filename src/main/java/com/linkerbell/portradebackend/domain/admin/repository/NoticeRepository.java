package com.linkerbell.portradebackend.domain.admin.repository;

import com.linkerbell.portradebackend.domain.admin.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findTopByIdIsGreaterThan(Long id);
    Optional<Notice> findTopByIdIsLessThan(Long id);
}
