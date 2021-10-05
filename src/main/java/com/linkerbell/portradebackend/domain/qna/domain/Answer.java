package com.linkerbell.portradebackend.domain.qna.domain;

import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@DiscriminatorValue("ANSWER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends Qna {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Builder
    public Answer(Long id, User user, String title, String content, boolean isPublic, Question question) {
        super(id, user, title, content, isPublic);
        this.question = question;
    }
}
