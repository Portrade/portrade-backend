package com.linkerbell.portradebackend.domain.qna.domain;

import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@ToString
@DiscriminatorValue("QUESTION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Qna {

    private String name;

    private String email;

    private String phoneNumber;

    private String category;

    //답변 완료 유무
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Question(Long id, User user, String title, String content, String category, Status status, boolean isPublic, String name, String email, String phoneNumber) {
        super(id, user, title, content, isPublic);
        this.category = category;
        this.status = status;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}
