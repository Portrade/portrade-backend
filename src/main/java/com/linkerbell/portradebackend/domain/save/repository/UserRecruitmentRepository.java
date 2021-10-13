package com.linkerbell.portradebackend.domain.save.repository;

import com.linkerbell.portradebackend.domain.save.domain.UserRecruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRecruitmentRepository extends JpaRepository<UserRecruitment, Long> {

    Page<UserRecruitment> findByUser_Id(Pageable pageable, UUID id);
}
