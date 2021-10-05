package com.linkerbell.portradebackend.domain.qna.service;

import com.linkerbell.portradebackend.domain.qna.domain.Answer;
import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import com.linkerbell.portradebackend.domain.qna.domain.Status;
import com.linkerbell.portradebackend.domain.qna.dto.CreateQnaRequestDto;
import com.linkerbell.portradebackend.domain.qna.dto.QnaDetailResponseDto;
import com.linkerbell.portradebackend.domain.qna.dto.QnasResponseDto;
import com.linkerbell.portradebackend.domain.qna.dto.ReplyQnaRequestDto;
import com.linkerbell.portradebackend.domain.qna.repository.QnaRepository;
import com.linkerbell.portradebackend.domain.user.domain.Role;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.exception.custom.NotExistException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
                .name("유저1")
                .birthDate("19900903")
                .wantedJob("designer")
                .build();

        admin = User.builder()
                .id(UUID.randomUUID())
                .username("admin1")
                .password("1234Aa@@")
                .name("관리자1")
                .birthDate("19870319")
                .wantedJob("security")
                .build();
        admin.addRole(Role.ROLE_ADMIN);

        question = Question.builder()
                .title("1대1문의글제목")
                .name("user1")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("업로드")
                .content("업로드 관련 문의드립니다.")
                .status(Status.UNANSWERED)
                .isPublic(false)
                .user(user)
                .build();
    }

    @DisplayName("1:1 문의 생성 성공")
    @Test
    void createQna() {
        //given
        CreateQnaRequestDto createRequestDto = CreateQnaRequestDto.builder()
                .title("1대1문의글제목")
                .name("user1")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("업로드")
                .content("업로드 관련 문의드립니다.")
                .isPublic(false)
                .build();

        //when
        qnaService.createQuestion(createRequestDto, user);

        //then
        verify(qnaRepository, times(1)).save(any(Qna.class));
    }

    @Test
    @DisplayName("1:1 문의 답변 생성 실패 - 존재하지 않는 ID")
    void createRelyQna_nonexistentId() {
        //given
        Long qnaId = 1L;
        given(qnaRepository.findByIdAndDType(qnaId)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(NotExistException.class,
                () -> qnaService.createAnswer(qnaId,any(ReplyQnaRequestDto.class), admin));
    }

    @Test
    @DisplayName("1:1 문의 답변 생성 성공 - ANSWERED 로 상태 변경")
    void createReplyQna() {
        //given
        ReplyQnaRequestDto replyQnaRequestDto = ReplyQnaRequestDto.builder()
                .title("1대1 문의 답변글!!")
                .content("1대1 문의 답변글 입니다.")
                .secret(false)
                .build();

        Long qnaId = 1L;
        given(qnaRepository.findByIdAndDType(qnaId))
                .willReturn(Optional.of(question));
        //when
        qnaService.createAnswer(qnaId, replyQnaRequestDto, admin);

        //then
        assertEquals(question.getStatus(), Status.ANSWERED);

        verify(qnaRepository, times(1)).findByIdAndDType(qnaId);
        verify(qnaRepository, times(1)).save(any(Answer.class));
    }

    @DisplayName("1:1 문의 목록 조회 성공")
    @Test
    void getQnas() {
        //given
        Qna qna1 = Question.builder()
                .title("질문")
                .content("질문있어요.")
                .name("김질문")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("programming")
                .status(Status.ANSWERED)
                .build();

        Qna qna2 = Answer.builder()
                .title("답변")
                .content("답변드립니다.")
                .question((Question) qna1)
                .build();

        Qna qna3 = Question.builder()
                .title("질문2")
                .content("질문있어요.")
                .name("이질문")
                .email("user2@gmail.com")
                .phoneNumber("12341234")
                .category("업로드")
                .status(Status.UNANSWERED)
                .build();

        List<Qna> qnas = new ArrayList<>(List.of(qna1, qna2, qna3));
        Page<Qna> page = new PageImpl<>(qnas);
        given(qnaRepository.findAll(any(Pageable.class))).willReturn(page);

        //when
        QnasResponseDto qnasResponseDto = qnaService.getQnas(1, 3);

        //then
        assertEquals(qnasResponseDto.getMaxPage(), 1);
        assertEquals(qnasResponseDto.getQnas().size(), 3);
    }

    @Test
    @DisplayName("1:1 문의 상세 조회 실패 - 존재하지 않는 ID")
    void getQna_nonexistentId(){
        //given
        Long qnaId = 1L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(NotExistException.class, () -> {
            qnaService.getQna(qnaId, user);
        });
    }

    @Test
    @DisplayName("1:1 문의 상세 조회 실패 - 비공개 글 조회, user 없음")
    void getQna_nouser(){
        //given
        Long qnaId = 1L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.of(question));

        //when
        //then
        assertThrows(UnAuthorizedException.class, () -> {
            qnaService.getQna(qnaId, null);
        });
    }

    @Test
    @DisplayName("1:1 문의 상세 조회 실패 - 비공개 글 조회, 권한 없음")
    public void getQna_unauthorized(){
        //given
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .username("user2")
                .password("1234Aa@@")
                .name("유저2")
                .birthDate("19900903")
                .wantedJob("designer")
                .build();

        Long qnaId = 1L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.of(question));

        //when
        //then
        assertThrows(UnAuthorizedException.class, () -> {
            qnaService.getQna(qnaId, user2);
        });
    }

    @Test
    @DisplayName("1:1 문의 상세 조회 성공 - 비공개 글 조회, 관리 계정")
    public void getQna_admin(){
        //given
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .name("유저1")
                .build();

        Qna qna1 = Question.builder()
                .id(1L)
                .title("질문")
                .content("질문있어요.")
                .name("김질문")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("업로드")
                .status(Status.ANSWERED)
                .user(user1)
                .build();

        User user3 = User.builder()
                .id(UUID.randomUUID())
                .username("user3")
                .name("유저1")
                .build();

        Qna qna3 = Question.builder()
                .id(3L)
                .title("질문2")
                .content("질문있어요.")
                .name("이질문")
                .email("user2@gmail.com")
                .phoneNumber("12341234")
                .category("업로드")
                .status(Status.UNANSWERED)
                .user(user3)
                .build();

        Long qnaId = 2L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.of(question));
        given(qnaRepository.findTopByIdIsGreaterThanOrderByIdAsc(qnaId)).willReturn(Optional.of(qna3));
        given(qnaRepository.findTopByIdIsLessThanOrderByIdDesc(qnaId)).willReturn(Optional.of(qna1));

        //when
        QnaDetailResponseDto qnaDetailResponseDto = qnaService.getQna(qnaId, admin);

        //then
        assertEquals(qnaDetailResponseDto.getId(), question.getId());
        assertEquals(qnaDetailResponseDto.getCreator(), question.getCreatorName());
        assertEquals(qnaDetailResponseDto.getNext().getId(), qna3.getId());
        assertEquals(qnaDetailResponseDto.getNext().getCreator(), qna3.getCreatorName());
        assertEquals(qnaDetailResponseDto.getPrev().getId(), qna1.getId());
        assertEquals(qnaDetailResponseDto.getPrev().getCreator(), qna1.getCreatorName());
    }

    @Test
    @DisplayName("1:1 문의 상세 조회 성공 - 비공개 글 조회, 주인 계정")
    public void getQna_user(){
        //given
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .name("유저1")
                .build();

        Qna qna1 = Question.builder()
                .id(1L)
                .title("질문")
                .content("질문있어요.")
                .name("김질문")
                .email("user1@gmail.com")
                .phoneNumber("12341234")
                .category("업로드")
                .status(Status.ANSWERED)
                .user(user1)
                .build();

        User user3 = User.builder()
                .id(UUID.randomUUID())
                .username("user3")
                .name("유저1")
                .build();

        Qna qna3 = Question.builder()
                .id(3L)
                .title("질문2")
                .content("질문있어요.")
                .name("이질문")
                .email("user2@gmail.com")
                .phoneNumber("12341234")
                .category("업로드")
                .status(Status.UNANSWERED)
                .user(user3)
                .build();

        Long qnaId = 2L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.of(question));
        given(qnaRepository.findTopByIdIsGreaterThanOrderByIdAsc(qnaId)).willReturn(Optional.of(qna3));
        given(qnaRepository.findTopByIdIsLessThanOrderByIdDesc(qnaId)).willReturn(Optional.of(qna1));

        //when
        QnaDetailResponseDto qnaDetailResponseDto = qnaService.getQna(qnaId, user);

        //then
        assertEquals(qnaDetailResponseDto.getId(), question.getId());
        assertEquals(qnaDetailResponseDto.getCreator(), question.getCreatorName());
        assertEquals(qnaDetailResponseDto.getNext().getId(), qna3.getId());
        assertEquals(qnaDetailResponseDto.getNext().getCreator(), qna3.getCreatorName());
        assertEquals(qnaDetailResponseDto.getPrev().getId(), qna1.getId());
        assertEquals(qnaDetailResponseDto.getPrev().getCreator(), qna1.getCreatorName());
    }

    @Test
    @DisplayName("1:1 문의 삭제 실패 - 존재 하지 않는 게시글 ID")
    public void deleteQna_nonexistentId(){
        //given
        Long qnaId = 1230L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(NotExistException.class, () -> {
            qnaService.deleteQna(qnaId, user);
        });
    }

    @Test
    @DisplayName("1:1 문의 삭제 실패 - 권한 없는 유저")
    public void test(){
        //given
        Long qnaId = 1L;
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .name("유저1")
                .build();

        given(qnaRepository.findById(qnaId)).willReturn(Optional.of(question));

        //when
        //then
        assertThrows(UnAuthorizedException.class, () -> {
           qnaService.deleteQna(qnaId, user1);
        });
    }

    @Test
    @DisplayName("1:1 문의 삭제 성공")
    public void deleteQna_user(){
        //given
        Long qnaId = 1L;
        given(qnaRepository.findById(qnaId)).willReturn(Optional.of(question));

        //when
        qnaService.deleteQna(qnaId, user);

        //then
        verify(qnaRepository, times(1)).findById(any());
        verify(qnaRepository, times(1)).delete(any(Qna.class));
    }
}