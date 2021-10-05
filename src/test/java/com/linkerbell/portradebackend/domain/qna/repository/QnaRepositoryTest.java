package com.linkerbell.portradebackend.domain.qna.repository;

import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@EnableJpaAuditing
class QnaRepositoryTest {

    @Autowired
    private QnaRepository qnaRepository;

    Question question;

    @BeforeEach
    public void setUp() {
        question = Question.builder()
                .title("1대1문의글제목")
                .name("user1")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("업로드")
                .content("업로드 관련 문의드립니다.")
                .isPublic(false)
                .build();

        qnaRepository.save(question);
    }

    @DisplayName("1:1문의 문의글 찾기")
    @Test
    void findQuestion() {
        //given
        //when
        Question foundQuestions = qnaRepository.findByIdAndDType(question.getId()).get();

        //then
        assertEquals(question.getTitle(),foundQuestions.getTitle());
        assertEquals(question.getName(),foundQuestions.getName());
        assertEquals(question.getEmail(),foundQuestions.getEmail());
        assertEquals(question.getCategory(),foundQuestions.getCategory());
        assertEquals(question.getContent(),foundQuestions.getContent());
        assertEquals(question.isPublic(),foundQuestions.isPublic());
    }

    @DisplayName("현재 id 이전 글 정보 가져오기")
    @Test
    void findLessThan() {
        //given
        Qna savedQna = qnaRepository.save(question);

        //when
        Qna qna = qnaRepository.findTopByIdIsLessThanOrderByIdDesc(savedQna.getId()+1).get();

        //then
        assertEquals(question.getTitle(), qna.getTitle());
        assertEquals(question.getContent(), qna.getContent());
        assertEquals(question.isPublic(), qna.isPublic());

    }

    @DisplayName("현재 id 이후 글 정보 가져오기")
    @Test
    void findGreaterThan() {
        //given
        Qna savedQna = qnaRepository.save(question);

        //when
        Qna qna = qnaRepository.findTopByIdIsGreaterThanOrderByIdAsc(savedQna.getId() - 1).get();

        //then
        assertEquals(question.getTitle(), qna.getTitle());
        assertEquals(question.getContent(), qna.getContent());
        assertEquals(question.isPublic(), qna.isPublic());
    }
}