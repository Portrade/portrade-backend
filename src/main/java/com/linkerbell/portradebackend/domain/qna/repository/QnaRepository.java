package com.linkerbell.portradebackend.domain.qna.repository;

import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    @Query("select q from Question q where q.id=:qnaId")
    Optional<Question> findByIdAndDType(@Param("qnaId") Long qnaId);
}
