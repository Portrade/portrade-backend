package com.linkerbell.portradebackend.domain.save.domain;

import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_recruitment")
public class UserRecruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_recruitment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @Builder
    public UserRecruitment(Long id, User user, Recruitment recruitment) {
        this.id = id;
        this.user = user;
        this.recruitment = recruitment;
    }
}
