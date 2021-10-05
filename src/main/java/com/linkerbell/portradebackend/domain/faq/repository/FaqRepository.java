package com.linkerbell.portradebackend.domain.faq.repository;

import com.linkerbell.portradebackend.domain.faq.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}
