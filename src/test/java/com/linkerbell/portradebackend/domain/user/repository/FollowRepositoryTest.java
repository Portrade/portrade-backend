package com.linkerbell.portradebackend.domain.user.repository;

import com.linkerbell.portradebackend.domain.user.domain.Follow;
import com.linkerbell.portradebackend.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Follow follow;

    @BeforeEach
    void setUp() {
        User users1 = User.builder()
                .username("users1")
                .password("1234Aa@@")
                .name("유저1")
                .birthDate("19900903")
                .wantedJob("programmer")
                .build();
        User users2 = User.builder()
                .username("users2")
                .password("1234Aa@@")
                .name("유저2")
                .birthDate("19900903")
                .wantedJob("designer")
                .build();
        user1 = userRepository.save(users1);
        user2 = userRepository.save(users2);

        follow = Follow.builder()
                .follower(user1)
                .following(user2)
                .build();
        followRepository.save(follow);
    }

    @DisplayName("팔로워/팔로윙 조회 성공")
    @Test
    void findByFollowerIdAndFollowingId() {
        //given
        //when
        Follow follow = followRepository.findByFollowerIdAndFollowingId(user1.getUsername(), user2.getUsername()).get();

        //then
        assertEquals("users2", follow.getFollowing().getUsername());
        assertEquals("유저2", follow.getFollowingName());
        assertEquals("users1", follow.getFollower().getUsername());
        assertEquals("유저1", follow.getFollower().getName());
    }

    @DisplayName("주어진 팔로워 id 가진 follow 갯수 조회 성공")
    @Test
    void countByFollowerId() {
        //given
        //when
        int count = followRepository.countByFollowing_Username(user2.getUsername());

        //then
        assertEquals(1, count);
    }

    @DisplayName("주어진 팔로잉 id 가진 follow 갯수 조회 성공")
    @Test
    public void countByFollowingId() {
        //given
        //when
        int count = followRepository.countByFollower_Username(user1.getUsername());

        //then
        assertEquals(1, count);
    }

    @DisplayName("주어진 팔로우 이름 가진 follow 조회 성공")
    @Test
    public void findAllByFollowerUsername() {
        //given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "id"));

        //when
        Page<Follow> followPage = followRepository.findAllByFollower_Username(pageable, user1.getUsername());

        //then
        assertEquals(1, followPage.getTotalElements());
        List<Follow> followers = followPage.getContent();
        assertEquals(user1.getName(), followers.get(0).getFollowerName());
    }

    @DisplayName("주어진 팔로잉 이름 가진 follow 조회 성공")
    @Test
    public void findAllByFollowingUsername() {
        //given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "id"));

        //when
        Page<Follow> followPage = followRepository.findAllByFollowing_Username(pageable, user2.getUsername());

        //then
        assertEquals(1, followPage.getTotalElements());
        List<Follow> followings = followPage.getContent();
        assertEquals(user2.getName(), followings.get(0).getFollowingName());
    }
}