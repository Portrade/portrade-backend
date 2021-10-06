package com.linkerbell.portradebackend.domain.faq.dto;

import com.linkerbell.portradebackend.domain.faq.domain.Faq;
import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateFaqRequestDto {

    @NotNull(message = "NULL_FAQ_TITLE")
    @Size(min=1, max=20, message = "INVALID_FAQ_TITLE")
    private String title;

    @NotNull(message = "NULL_FAQ_CONTENT")
    @Size(min=1, max=500, message = "INVALID_FAQ_CONTENT")
    private String content;

    @Builder
    public CreateFaqRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Faq toEntity(User user) {
        return Faq.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
    }
}
