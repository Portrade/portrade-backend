package com.linkerbell.portradebackend.domain.qna.service;

import com.linkerbell.portradebackend.domain.qna.domain.Answer;
import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import com.linkerbell.portradebackend.domain.qna.domain.Status;
import com.linkerbell.portradebackend.domain.qna.dto.AnswerRequestDto;
import com.linkerbell.portradebackend.domain.qna.dto.QnaDetailResponseDto;
import com.linkerbell.portradebackend.domain.qna.dto.QnasResponseDto;
import com.linkerbell.portradebackend.domain.qna.dto.QuestionRequestDto;
import com.linkerbell.portradebackend.domain.qna.repository.QnaRepository;
import com.linkerbell.portradebackend.domain.user.domain.Role;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {

    @InjectMocks
    private QnaService qnaService;
    @Mock
    private QnaRepository qnaRepository;

    private User user;
    private User admin;
    private Question question;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .password("1234Aa@@")
                .name("??????1")
                .birthDate("19900903")
                .wantedJob("designer")
                .build();
        admin = User.builder()
                .id(UUID.randomUUID())
                .username("admin1")
                .password("1234Aa@@")
                .name("?????????1")
                .birthDate("19870319")
                .wantedJob("security")
                .build();
        admin.addRole(Role.ROLE_ADMIN);

        question = Question.builder()
                .title("1???1???????????????")
                .name("user1")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("?????????")
                .content("????????? ?????? ??????????????????.")
                .status(Status.UNANSWERED)
                .isPublic(false)
                .user(user)
                .build();
    }

    @DisplayName("1:1 ?????? ?????? ??????")
    @Test
    void createQna() {
        //given
        QuestionRequestDto createRequestDto = QuestionRequestDto.builder()
                .title("1???1???????????????")
                .name("user1")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("?????????")
                .content("????????? ?????? ??????????????????.")
                .isPublic(false)
                .build();

        //when
        qnaService.createQuestion(createRequestDto, user);

        //then
        verify(qnaRepository, times(1)).save(any(Qna.class));
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ?????? ?????? - ???????????? ?????? ID")
    void createRelyQna_nonexistentId() {
        //given
        Long qnaId = 1L;
        given(qnaRepository.findByIdAndDType(qnaId))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(NonExistentException.class,
                () -> qnaService.createAnswer(qnaId, any(AnswerRequestDto.class), admin));
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ?????? ?????? - ANSWERED ??? ?????? ??????")
    void createReplyQna() {
        //given
        Long qnaId = 1L;
        AnswerRequestDto answerRequestDto = AnswerRequestDto.builder()
                .title("1???1 ?????? ?????????!!")
                .content("1???1 ?????? ????????? ?????????.")
                .isPublic(false)
                .build();

        given(qnaRepository.findByIdAndDType(qnaId))
                .willReturn(Optional.of(question));

        //when
        qnaService.createAnswer(qnaId, answerRequestDto, admin);

        //then
        assertEquals(question.getStatus(), Status.ANSWERED);
        verify(qnaRepository, times(1)).findByIdAndDType(qnaId);
        verify(qnaRepository, times(1)).save(any(Answer.class));
    }

    @DisplayName("1:1 ?????? ?????? ?????? ??????")
    @Test
    void getQnas() {
        //given
        Question qna1 = Question.builder()
                .title("??????")
                .content("???????????????.")
                .name("?????????")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("programming")
                .status(Status.ANSWERED)
                .isPublic(true)
                .build();
        Qna qna2 = Answer.builder()
                .title("??????")
                .content("??????????????????.")
                .question(qna1)
                .isPublic(true)
                .build();
        Qna qna3 = Question.builder()
                .title("??????2")
                .content("???????????????.")
                .name("?????????")
                .email("user2@gmail.com")
                .phoneNumber("12341234")
                .category("?????????")
                .status(Status.UNANSWERED)
                .isPublic(true)
                .build();
        List<Qna> qnas = new ArrayList<>(List.of(qna1, qna2, qna3));
        Page<Qna> page = new PageImpl<>(qnas);

        given(qnaRepository.findAllByTitleContainingAndContentContainingIgnoreCase(any(Pageable.class), anyString(), anyString()))
                .willReturn(page);

        //when
        QnasResponseDto qnasResponseDto = qnaService.getQnas(1, 3, "", "all");

        //then
        assertEquals(qnasResponseDto.getPage().getTotalPage(), 1);
        assertEquals(qnasResponseDto.getQnas().size(), 3);
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ?????? ?????? - ???????????? ?????? ID")
    void getQna_nonexistentId() {
        //given
        Long qnaId = 1L;
        given(qnaRepository.findById(qnaId))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(NonExistentException.class,
                () -> qnaService.getQna(qnaId, user));
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ?????? ?????? - ????????? ??? ??????, ????????????")
    void getQna_notLoggedIn() {
        //given
        Long qnaId = 1L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.of(question));

        //when
        //then
        assertThrows(UnAuthorizedException.class,
                () -> qnaService.getQna(qnaId, null));
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ?????? ?????? - ????????? ??? ??????, ?????? ??????")
    public void getQna_unauthorized() {
        //given
        Long qnaId = 1L;
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .username("user2")
                .password("1234Aa@@")
                .name("??????2")
                .birthDate("19900903")
                .wantedJob("designer")
                .build();

        given(qnaRepository.findById(qnaId))
                .willReturn(Optional.of(question));

        //when
        //then
        assertThrows(UnAuthorizedException.class,
                () -> qnaService.getQna(qnaId, user2));
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ?????? ?????? - ????????? ??? ??????, ?????? ??????")
    public void getQna_admin() {
        //given
        Long qnaId = 2L;
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .name("??????1")
                .build();
        Qna qna1 = Question.builder()
                .id(1L)
                .title("??????")
                .content("???????????????.")
                .name("?????????")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("?????????")
                .status(Status.ANSWERED)
                .user(user1)
                .build();
        User user3 = User.builder()
                .id(UUID.randomUUID())
                .username("user3")
                .name("??????1")
                .build();
        Qna qna3 = Question.builder()
                .id(3L)
                .title("??????2")
                .content("???????????????.")
                .name("?????????")
                .email("user2@gmail.com")
                .phoneNumber("12341234")
                .category("?????????")
                .status(Status.UNANSWERED)
                .user(user3)
                .build();

        given(qnaRepository.findById(qnaId))
                .willReturn(Optional.of(question));
        given(qnaRepository.findTopByIdIsGreaterThanOrderByIdAsc(qnaId))
                .willReturn(Optional.of(qna3));
        given(qnaRepository.findTopByIdIsLessThanOrderByIdDesc(qnaId))
                .willReturn(Optional.of(qna1));

        //when
        QnaDetailResponseDto qnaDetailResponseDto = qnaService.getQna(qnaId, admin);

        //then
        assertEquals(qnaDetailResponseDto.getId(), question.getId());
        assertEquals(qnaDetailResponseDto.getCreator(), question.getCreatorName());
        assertEquals(qnaDetailResponseDto.getNext().getId(), qna3.getId());
        assertEquals(qnaDetailResponseDto.getPrev().getId(), qna1.getId());
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ?????? ?????? - ????????? ??? ??????, ?????? ??????")
    public void getQna_user() {
        //given
        Long qnaId = 2L;
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .name("??????1")
                .build();
        Qna qna1 = Question.builder()
                .id(1L)
                .title("??????")
                .content("???????????????.")
                .name("?????????")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("?????????")
                .status(Status.ANSWERED)
                .user(user1)
                .build();
        User user3 = User.builder()
                .id(UUID.randomUUID())
                .username("user3")
                .name("??????1")
                .build();
        Qna qna3 = Question.builder()
                .id(3L)
                .title("??????2")
                .content("???????????????.")
                .name("?????????")
                .email("user2@gmail.com")
                .phoneNumber("12341234")
                .category("?????????")
                .status(Status.UNANSWERED)
                .user(user3)
                .build();

        given(qnaRepository.findById(qnaId))
                .willReturn(Optional.of(question));
        given(qnaRepository.findTopByIdIsGreaterThanOrderByIdAsc(qnaId))
                .willReturn(Optional.of(qna3));
        given(qnaRepository.findTopByIdIsLessThanOrderByIdDesc(qnaId))
                .willReturn(Optional.of(qna1));

        //when
        QnaDetailResponseDto qnaDetailResponseDto = qnaService.getQna(qnaId, user);

        //then
        assertEquals(qnaDetailResponseDto.getId(), question.getId());
        assertEquals(qnaDetailResponseDto.getCreator(), question.getCreatorName());
        assertEquals(qnaDetailResponseDto.getNext().getId(), qna3.getId());
        assertEquals(qnaDetailResponseDto.getPrev().getId(), qna1.getId());
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ?????? - ?????? ?????? ?????? ????????? ID")
    public void deleteQna_nonexistentId() {
        //given
        Long qnaId = 1230L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(NonExistentException.class,
                () -> qnaService.deleteQna(qnaId, user));
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ?????? - ???????????? ??????")
    public void test() {
        //given
        Long qnaId = 1L;
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .name("??????1")
                .build();

        given(qnaRepository.findById(qnaId))
                .willReturn(Optional.of(question));

        //when
        //then
        assertThrows(UnAuthorizedException.class,
                () -> qnaService.deleteQna(qnaId, user1));
    }

    @Test
    @DisplayName("1:1 ?????? ?????? ??????")
    public void deleteQna_user() {
        //given
        Long qnaId = 1L;
        given(qnaRepository.findById(qnaId))
                .willReturn(Optional.of(question));

        //when
        qnaService.deleteQna(qnaId, user);

        //then
        verify(qnaRepository, times(1)).findById(any());
        verify(qnaRepository, times(1)).delete(any(Qna.class));
    }
}