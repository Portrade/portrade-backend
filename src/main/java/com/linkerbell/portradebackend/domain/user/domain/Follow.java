package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

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
    @JoinColumn(name = "following")
    private User following;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower")
    private User follower;

    @Builder
    public Follow(Long id, User following, User follower) {
        this.id = id;
        this.following = following;
        this.follower = follower;
    }

    public UUID getFollowingId() {
        return following.getId();
    }
    public String getFollowingName() {
        return following.getName();
    }

    public Profile getFollowingProfile() {
        return following.getProfile();
    }

    public String getFollowingProfileUrl() {
        return getFollowingProfile().getProfileUrl();
    }

    public String getFollowingJob() {
        return getFollowingProfile().getJob();
    }

    public UUID getFollowerId() {
        return follower.getId();
    }
    public String getFollowerName() {
        return follower.getName();
    }

    public Profile getFollowerProfile() {
        return follower.getProfile();
    }

    public String getFollowerProfileUrl() {
        return getFollowerProfile().getProfileUrl();
    }

    public String getFollowerJob() {
        return getFollowerProfile().getJob();
    }
}
