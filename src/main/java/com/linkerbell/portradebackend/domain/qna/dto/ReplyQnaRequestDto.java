package com.linkerbell.portradebackend.domain.qna.dto;

import com.linkerbell.portradebackend.domain.qna.domain.Answer;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyQnaRequestDto {
    @NotNull(message = "NULL_QNA_TITLE")
    @Size(min = 1, max = 20, message = "INVALID_QNA_TITLE")
    private String title;

    @NotNull(message = "NULL_QNA_CONTENT")
    @Size(min = 1, max = 500, message = "INVALID_QNA_CONTENT")
    private String content;

    @NotNull(message = "NULL_QNA_ISPUBLIC")
    private boolean secret;

    @Builder
    public ReplyQnaRequestDto(String title, String content, boolean secret) {
        this.title = title;
        this.content = content;
        this.secret = secret;
    }

    public Answer toEntity(User user, Question question) {
        return Answer.builder()
                .title(title)
                .content(content)
                .isPublic(secret)
                .user(user)
                .question(question)
                .build();
    }
}
