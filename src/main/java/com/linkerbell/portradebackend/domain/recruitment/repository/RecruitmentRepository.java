package com.linkerbell.portradebackend.domain.recruitment.repository;

import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    Page<Recruitment> findAllByCompany_Id(Pageable pageable, Long companyId);
}
