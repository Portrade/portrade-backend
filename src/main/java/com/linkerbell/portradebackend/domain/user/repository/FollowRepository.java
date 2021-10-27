package com.linkerbell.portradebackend.domain.user.repository;

import com.linkerbell.portradebackend.domain.user.domain.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("select f from Follow f join f.follower u1 join f.following u2 where u1.username = :follower and u2.username = :following")
    Optional<Follow> findByFollowerIdAndFollowingId(@Param("follower") String follower, @Param("following") String following);

    Long countByFollower_Id(UUID id);

    Long countByFollowing_Id(UUID id);

    Page<Follow> findAllByFollower_Username(Pageable pageable, String userId);

    Page<Follow> findAllByFollowing_Username(Pageable pageable, String userId);
}
