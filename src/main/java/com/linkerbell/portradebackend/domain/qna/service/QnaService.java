package com.linkerbell.portradebackend.domain.qna.service;

import com.linkerbell.portradebackend.domain.qna.domain.Answer;
import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import com.linkerbell.portradebackend.domain.qna.domain.Status;
import com.linkerbell.portradebackend.domain.qna.dto.*;
import com.linkerbell.portradebackend.domain.qna.repository.QnaRepository;
import com.linkerbell.portradebackend.domain.qna.repository.QuestionRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private final QnaRepository qnaRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public IdResponseDto createQuestion(QuestionRequestDto requestDto, User user) {
        Question qna = Question.builder()
                .category(requestDto.getCategory())
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .email(requestDto.getEmail())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .isPublic(requestDto.getIsPublic())
                .user(user)
                .status(Status.UNANSWERED)
                .build();
        qnaRepository.save(qna);

        return new IdResponseDto(qna.getId());
    }

    @Transactional
    public IdResponseDto createAnswer(Long qnaId, AnswerRequestDto requestDto, User user) {
        Question foundQna = qnaRepository.findByIdAndDType(qnaId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_QNA_ID));

        Answer answer = requestDto.toEntity(user, foundQna);
        qnaRepository.save(answer);
        foundQna.changeStatus(Status.ANSWERED);

        return new IdResponseDto(answer.getId());
    }

    public QnasResponseDto getQnas(int page, int size, String keyword, String type) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<Qna> qnaPage = null;
        switch (type) {
            case "answered":
                qnaPage = questionRepository.findAllByTitleContainingAndContentContainingIgnoreCaseAndStatusEquals(pageable, keyword, keyword, Status.ANSWERED);
                break;
            case "unanswered":
                qnaPage = questionRepository.findAllByTitleContainingAndContentContainingIgnoreCaseAndStatusEquals(pageable, keyword, keyword, Status.UNANSWERED);
                break;
            default:
                qnaPage = qnaRepository.findAllByTitleContainingAndContentContainingIgnoreCase(pageable, keyword, keyword);
                break;
        }

        List<QnaResponseDto> qnaResponseDtos = qnaPage.stream()
                .map(QnaResponseDto::of)
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(qnaPage.getTotalPages())
                .totalElement(qnaPage.getTotalElements())
                .build();

        return QnasResponseDto.builder()
                .page(pageResponseDto)
                .qnas(qnaResponseDtos)
                .build();
    }

    public QnaDetailResponseDto getQna(Long qnaId, User user) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_QNA));

        if (!qna.isPublic()) {
            if (Objects.isNull(user) || !user.equals(qna.getUser()) && !user.isAdmin()) {
                throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);
            }
        }

        Optional<Qna> nextQnaOptional = qnaRepository.findTopByIdIsGreaterThanOrderByIdAsc(qnaId);
        Optional<Qna> prevQnaOptional = qnaRepository.findTopByIdIsLessThanOrderByIdDesc(qnaId);

        QnaResponseDto nextQnaResponseDto = nextQnaOptional.isPresent()
                ? QnaResponseDto.of(nextQnaOptional.get())
                : null;

        QnaResponseDto prevQnaResponseDto = prevQnaOptional.isPresent()
                ? QnaResponseDto.of(prevQnaOptional.get())
                : null;

        return QnaDetailResponseDto.builder()
                .id(qna.getId())
                .creator(qna.getCreatorName())
                .title(qna.getTitle())
                .content(qna.getContent())
                .isPublic(qna.isPublic())
                .createdDate(qna.getCreatedDate())
                .lastModifiedDate(qna.getLastModifiedDate())
                .next(nextQnaResponseDto)
                .prev(prevQnaResponseDto)
                .build();
    }

    @Transactional
    public void deleteQna(Long qnaId, User user) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_QNA));
        if (user.equals(qna.getUser()) || user.isAdmin())
            qnaRepository.delete(qna);
        else
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);
    }
}
