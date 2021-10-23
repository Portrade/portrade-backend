package com.linkerbell.portradebackend.domain.qna.service;

import com.linkerbell.portradebackend.domain.qna.domain.Answer;
import com.linkerbell.portradebackend.domain.qna.dto.*;
import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import com.linkerbell.portradebackend.domain.qna.domain.Status;
import com.linkerbell.portradebackend.domain.qna.repository.QnaRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NotExistException;
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

    @Transactional
    public CreateResponseDto createQuestion(CreateQnaRequestDto requestDto, User user) {
        Question qna = requestDto.toEntity(user, Status.UNANSWERED);
        qnaRepository.save(qna);
        return new CreateResponseDto(qna.getId());
    }

    @Transactional
    public CreateResponseDto createAnswer(Long qnaId, ReplyQnaRequestDto requestDto, User user) {
        Question foundQna = qnaRepository.findByIdAndDType(qnaId)
                .orElseThrow(() -> new NotExistException(ErrorCode.NONEXISTENT_QNA_ID));

        Answer answer = requestDto.toEntity(user, foundQna);
        qnaRepository.save(answer);
        foundQna.changeStatus(Status.ANSWERED);

        return new CreateResponseDto(answer.getId());
    }

    public QnasResponseDto getQnas(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(
                page-1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<Qna> pageQnas = null;
        if(keyword.equals(""))
            pageQnas = qnaRepository.findAll(pageable);
        else
            pageQnas = qnaRepository.findAllByTitleContainingAndContentContainingIgnoreCase(pageable, keyword, keyword);

        List<QnaResponseDto> qnaResponseDto = pageQnas.stream()
                .map(qna -> QnaResponseDto.toDto(qna))
                .collect(Collectors.toList());

        return QnasResponseDto.builder()
                .qnas(qnaResponseDto)
                .maxPage(pageQnas.getTotalPages())
                .build();
    }

    public QnaDetailResponseDto getQna(Long qnaId, User user) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new NotExistException(ErrorCode.NONEXISTENT_QNA));

        if (!qna.isPublic()) {
            if(Objects.isNull(user) || !user.equals(qna.getUser()) && !user.isAdmin()) {
                throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);
            }
        }

        Optional<Qna> nextQnaOptional = qnaRepository.findTopByIdIsGreaterThanOrderByIdAsc(qnaId);
        Optional<Qna> prevQnaOptional = qnaRepository.findTopByIdIsLessThanOrderByIdDesc(qnaId);

        QnaNextDetailResponseDto nextQnaResponseDto = nextQnaOptional.isPresent()
                ? QnaNextDetailResponseDto.of(nextQnaOptional.get())
                : null;

        QnaNextDetailResponseDto prevQnaResponseDto = prevQnaOptional.isPresent()
                ? QnaNextDetailResponseDto.of(prevQnaOptional.get())
                : null;

        return QnaDetailResponseDto.builder()
                .id(qna.getId())
                .creator(qna.getCreatorName())
                .title(qna.getTitle())
                .content(qna.getContent())
                .secret(qna.isPublic())
                .createdDate(qna.getCreatedDate())
                .lastModifiedDate(qna.getLastModifiedDate())
                .next(nextQnaResponseDto)
                .prev(prevQnaResponseDto)
                .build();
    }

    @Transactional
    public void deleteQna(Long qnaId, User user) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new NotExistException(ErrorCode.NONEXISTENT_QNA));
        if(user.equals(qna.getUser()) || user.isAdmin())
            qnaRepository.delete(qna);
        else
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);
    }
}
