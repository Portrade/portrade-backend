package com.linkerbell.portradebackend.domain.comment.service;

import com.linkerbell.portradebackend.domain.comment.domain.Comment;
import com.linkerbell.portradebackend.domain.comment.dto.CommentRequestDto;
import com.linkerbell.portradebackend.domain.comment.dto.CommentsResponseDto;
import com.linkerbell.portradebackend.domain.comment.repository.CommentRepository;
import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PortfolioRepository portfolioRepository;

    private User user;
    private Portfolio portfolio;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("user5")
                .password("1234Aa@@")
                .name("회원1")
                .birthDate("20030327")
                .wantedJob("marketing")
                .build();

        portfolio = Portfolio.builder()
                .id(1L)
                .creator(user)
                .title("포트폴리오 제목1")
                .description("포트폴리오 설명1")
                .category("카테고리")
                .isPublic(true)
                .viewCount(10)
                .contentFiles(Collections.emptyList())
                .likes(Collections.emptyList())
                .comments(Collections.emptyList())
                .build();
    }

    @DisplayName("댓글 생성 성공")
    @Test
    void createComment() throws Exception {
        // given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("댓글 내용")
                .build();

        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(portfolio));

        // when
        commentService.createComment(commentRequestDto, 1L, user);

        // then
        verify(commentRepository, times(1))
                .save(any(Comment.class));
    }

    @DisplayName("댓글 목록 조회 성공")
    @Test
    void getComments() throws Exception {
        // given
        Comment comment1 = Comment.builder()
                .content("댓글 내용1")
                .user(user)
                .build();
        Comment comment2 = Comment.builder()
                .content("댓글 내용2")
                .user(user)
                .build();
        Comment comment3 = Comment.builder()
                .content("댓글 내용3")
                .user(user)
                .build();
        List<Comment> comments = new ArrayList<>(List.of(comment1, comment2, comment3));
        Page<Comment> commentPage = new PageImpl<>(comments);

        given(portfolioRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(portfolio));
        given(commentRepository.findAllByPortfolio_Id(any(Pageable.class), anyLong()))
                .willReturn(commentPage);

        // when
        CommentsResponseDto foundCommentsResponseDto = commentService.getComments(1L, 1, 5);

        // then
        assertEquals(foundCommentsResponseDto.getPage().getTotalPage(), 1);
        assertEquals(foundCommentsResponseDto.getPage().getTotalElement(), 3);
    }
}