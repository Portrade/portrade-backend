package com.linkerbell.portradebackend.domain.follow.domain;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follow")
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_user_id")
    private User followUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id")
    private User followingUser;

    @Builder
    public Follow(Long id, User followUser, User followingUser) {
        this.id = id;
        this.followUser = followUser;
        this.followingUser = followingUser;
    }
}
