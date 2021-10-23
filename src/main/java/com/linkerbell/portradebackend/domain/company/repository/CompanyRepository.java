package com.linkerbell.portradebackend.domain.company.repository;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByNameAndCeo(String name, String ceo);

    @Query("select c from Company c join fetch c.user u where c.id = :id")
    Optional<Company> findById(@Param("id") Long id);
}
