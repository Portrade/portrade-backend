package com.linkerbell.portradebackend.domain.qna.dto;

import com.linkerbell.portradebackend.domain.qna.domain.Question;
import com.linkerbell.portradebackend.domain.qna.domain.Status;
import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateQnaRequestDto {

    @NotNull(message = "NULL_QNA_CATEGORY")
    private String category;

    @NotNull(message = "NULL_USER_NAME")
    @Size(min=2, max=8, message = "INVALID_USER_NAME")
    private String name;

    @NotNull(message = "NULL_QNA_PHONENUMBER")
    @Size(min=8, max=8, message = "INVALID_QNA_PHONENUMBER")
    private String phoneNumber;

    @NotNull(message = "NULL_QNA_EMAIL")
    @Email(message = "INVALID_QNA_EMAIL")
    private String email;

    @NotNull(message = "NULL_QNA_TITLE")
    @Size(min=1, max=20, message = "INVALID_QNA_TITLE")
    private String title;

    @NotNull(message = "NULL_QNA_CONTENT")
    @Size(min=1, max=500, message = "INVALID_QNA_CONTENT")
    private String content;

    private boolean isPublic;  //기본값 true


    @Builder
    public CreateQnaRequestDto(String category, String name, String phoneNumber, String email, String title, String content, boolean isPublic) {
        this.category = category;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
    }

    public Question toEntity(User user, Status status) {
        return Question.builder()
                .category(category)
                .name(name)
                .phoneNumber(phoneNumber)
                .email(email)
                .title(title)
                .content(content)
                .isPublic(isPublic)
                .user(user)
                .status(status)
                .build();
    }
}
