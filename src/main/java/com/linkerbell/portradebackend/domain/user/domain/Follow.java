package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = {"following", "follower"})
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

    public String getFollowingName() {
        return following.getName();
    }

    public String getFollowingUsername() {
        return following.getUsername();
    }

    public String getFollowerUsername() {
        return follower.getUsername();
    }

    public Profile getFollowingProfile() {
        return following.getProfile();
    }

    public String getFollowingProfileUrl() {
        return getFollowingProfile().getProfileImageFile().getUrl();
    }

    public String getFollowingJob() {
        return getFollowingProfile().getJob();
    }

    public String getFollowerName() {
        return follower.getName();
    }

    public Profile getFollowerProfile() {
        return follower.getProfile();
    }

    public String getFollowerProfileUrl() {
        return getFollowerProfile().getProfileImageFile().getUrl();
    }

    public String getFollowerJob() {
        return getFollowerProfile().getJob();
    }
}
