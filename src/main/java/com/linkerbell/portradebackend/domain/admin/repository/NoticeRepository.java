package com.linkerbell.portradebackend.domain.admin.repository;

import com.linkerbell.portradebackend.domain.admin.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
