package com.linkerbell.portradebackend.domain.qna.repository;

import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import com.linkerbell.portradebackend.domain.qna.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Qna> findAllByTitleContainingAndContentContainingIgnoreCaseAndStatusEquals(Pageable pageable, String title, String content, Status status);
}
