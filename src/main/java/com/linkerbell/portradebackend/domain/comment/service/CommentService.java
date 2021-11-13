package com.linkerbell.portradebackend.domain.comment.service;

import com.linkerbell.portradebackend.domain.comment.domain.Comment;
import com.linkerbell.portradebackend.domain.comment.dto.CommentRequestDto;
import com.linkerbell.portradebackend.domain.comment.dto.CommentResponseDto;
import com.linkerbell.portradebackend.domain.comment.dto.CommentsResponseDto;
import com.linkerbell.portradebackend.domain.comment.repository.CommentRepository;
import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthenticatedException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PortfolioRepository portfolioRepository;

    private void checkUserPermission(Comment comment, User user) {
        if (Objects.isNull(user)) {
            throw new UnAuthenticatedException(ErrorCode.NONEXISTENT_AUTHENTICATION);
        } else if (!user.equals(comment.getCreator()) && !user.isAdmin()) {
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);
        }
    }

    @Transactional
    public IdResponseDto createComment(CommentRequestDto commentRequestDto, Long portfolioId, User user) {
        if (user == null) {
            throw new UnAuthenticatedException(ErrorCode.NONEXISTENT_AUTHENTICATION);
        }

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_PORTFOLIO));

        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .portfolio(portfolio)
                .creator(user)
                .build();
        commentRepository.save(comment);

        return new IdResponseDto(comment.getId());
    }

    public CommentsResponseDto getComments(Long portfolioId, int page, int size) {
        portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_PORTFOLIO));

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Comment> commentPage = commentRepository.findAllByPortfolio_Id(pageable, portfolioId);

        List<CommentResponseDto> commentResponseDtos = commentPage.stream()
                .map(CommentResponseDto::of)
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(commentPage.getTotalPages())
                .totalElement(commentPage.getTotalElements())
                .build();

        return CommentsResponseDto.builder()
                .page(pageResponseDto)
                .comments(commentResponseDtos)
                .build();
    }

    @Transactional
    public void deleteComment(Long portfolioId, Long commentId, User user) {
        portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_PORTFOLIO));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_COMMENT));

        checkUserPermission(comment, user);

        commentRepository.delete(comment);
    }
}
