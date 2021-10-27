package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Follow;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.FollowersResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.FollowingsResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileResponeDto;
import com.linkerbell.portradebackend.domain.user.repository.FollowRepository;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void followUser(String follower, String following, User user) {
        User followUser = userRepository.findByUsername(following)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_USER));

        if (!user.getUsername().equals(follower))
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);

        Optional<Follow> result = followRepository.findByFollowerIdAndFollowingId(follower, following);

        // 팔로우 상태 체크 - 팔로우 된 상태
        if (result.isPresent()) {
            followRepository.delete(result.get());
        } else {
            Follow follow = Follow.builder()
                    .follower(user)
                    .following(followUser)
                    .build();

            followRepository.save(follow);
        }
    }

    // 나를 팔로우 하는 user 조회
    public FollowersResponseDto getFollowers(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<Follow> followUser = followRepository.findAllByFollowing_Username(pageable, userId);
        List<ProfileResponeDto> followers = followUser
                .stream()
                .map(follow -> ProfileResponeDto.builder()
                        .id(follow.getFollowerId())
                        .name(follow.getFollowerName())
                        .profileUrl(follow.getFollowerProfileUrl())
                        .job(follow.getFollowerJob())
                        .build())
                .collect(Collectors.toList());

        return FollowersResponseDto.builder()
                .followers(followers)
                .maxPage(followUser.getTotalPages())
                .build();
    }

    // 내가 팔로우 하는
    public FollowingsResponseDto getFollowings(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<Follow> followUser = followRepository.findAllByFollower_Username(pageable, userId);
        List<ProfileResponeDto> followers = followUser
                .stream()
                .map(follow -> ProfileResponeDto.builder()
                        .id(follow.getFollowingId())
                        .name(follow.getFollowingName())
                        .profileUrl(follow.getFollowingProfileUrl())
                        .job(follow.getFollowingJob())
                        .build())
                .collect(Collectors.toList());

        return FollowingsResponseDto.builder()
                .followings(followers)
                .maxPage(followUser.getTotalPages())
                .build();
    }
}
