package com.linkerbell.portradebackend.domain.company.repository;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByNameAndCeo(String name, String ceo);
}
