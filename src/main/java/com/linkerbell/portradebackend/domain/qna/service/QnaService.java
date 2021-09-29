package com.linkerbell.portradebackend.domain.qna.service;


import com.linkerbell.portradebackend.domain.qna.dto.*;
import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import com.linkerbell.portradebackend.domain.qna.domain.Status;
import com.linkerbell.portradebackend.domain.qna.mapper.QnaDetailMapper;
import com.linkerbell.portradebackend.domain.qna.mapper.QnaMapper;
import com.linkerbell.portradebackend.domain.qna.repository.QnaRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NotExsitException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private QnaMapper qnaMapper = Mappers.getMapper(QnaMapper.class);
    private QnaDetailMapper qnaDetailMapper = Mappers.getMapper(QnaDetailMapper.class);
    private final QnaRepository qnaRepository;

    @Transactional
    public CreateQnaResponseDto createQna(CreateQnaRequestDto requestDto, User user) {
        Question qna = qnaMapper.toEntity(requestDto, user, Status.UNANSWERED);
        Qna savedQna = qnaRepository.save(qna);
        return CreateQnaResponseDto.builder()
                .id(savedQna.getId())
                .build();
    }

    @Transactional
    public ReplyQnaResponseDto createReplyQna(Long qnaId, ReplyQnaRequestDto requestDto, User user) {
        Question foundQna = qnaRepository.findByIdAndDType(qnaId)
                .orElseThrow(() -> new NotExsitException(ErrorCode.NONEXISTENT_QNA_ID));

        Qna qna = qnaMapper.toEntity(requestDto, user, foundQna);
        Qna savedQna = qnaRepository.save(qna);
        foundQna.changeStatus(Status.ANSWERED);

        return ReplyQnaResponseDto.builder()
                .id(savedQna.getId())
                .build();
    }

    public QnasResponseDto getQnas(int page, int size) {
        Pageable pageable = PageRequest.of(
                page-1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<Qna> pageQnas = qnaRepository.findAll(pageable);
        List<QnaResponseDto> qnaResponseDto = pageQnas.stream()
                .map(qna -> qnaMapper.toDto(qna))
                .collect(Collectors.toList());
        return new QnasResponseDto(qnaResponseDto, pageQnas.getTotalPages());
    }

    public QnaDetailResponseDto getQna(Long qnaId, User user) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new NotExsitException(ErrorCode.NONEXISTENT_QNA));

        if (!qna.isPublic() && !user.getId().equals(qna.Id())) {
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORITY);
        }

        QnaCurDetailResponseDto curDetailResponseDto = qnaDetailMapper.toDto(qna);
//        QnaNextDetailResponseDto nextResponseDto = qnaDetailMapper.toDto(qna);
        return new QnaDetailResponseDto(curDetailResponseDto, new QnaNextDetailResponseDto(6L, "admin", "1:1문의글", null),
                new QnaNextDetailResponseDto(6L, "admin", "1:1문의글", null));
    }
}