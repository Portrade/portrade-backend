package com.linkerbell.portradebackend.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class QuestionRequestDto {

    @NotNull(message = "NULL_QNA_CATEGORY")
    private String category;

    @NotNull(message = "NULL_USER_NAME")
    @Size(min = 2, max = 8, message = "INVALID_USER_NAME")
    private String name;

    @NotNull(message = "NULL_QNA_PHONENUMBER")
    @Size(min = 8, max = 8, message = "INVALID_QNA_PHONENUMBER")
    private String phoneNumber;

    @NotNull(message = "NULL_QNA_EMAIL")
    @Email(message = "INVALID_QNA_EMAIL")
    private String email;

    @NotNull(message = "NULL_QNA_TITLE")
    @Size(min = 1, max = 20, message = "INVALID_QNA_TITLE")
    private String title;

    @NotNull(message = "NULL_QNA_CONTENT")
    @Size(min = 1, max = 500, message = "INVALID_QNA_CONTENT")
    private String content;

    @NotNull(message = "NULL_QNA_ISPUBLIC")
    private Boolean isPublic;

    @Builder
    private QuestionRequestDto(String category, String name, String phoneNumber, String email, String title, String content, Boolean isPublic) {
        this.category = category;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
    }
}
