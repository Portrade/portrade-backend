package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Follow;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.FollowersResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.FollowingsResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.FollowRepository;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthenticatedException;
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
    public boolean followUser(String follower, String following, User user) {
        User followUser = userRepository.findByUsername(following)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_USER));
        userRepository.findByUsername(follower)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_USER));

        if (user == null) {
            throw new UnAuthenticatedException(ErrorCode.NONEXISTENT_AUTHENTICATION);
        }
        if (!user.getUsername().equals(follower)) {
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);
        }

        Optional<Follow> result = followRepository.findByFollowerIdAndFollowingId(follower, following);
        if (result.isPresent()) {
            followRepository.delete(result.get());
            return false;
        } else {
            Follow follow = Follow.builder()
                    .follower(user)
                    .following(followUser)
                    .build();
            followRepository.save(follow);
            return true;
        }
    }

    public FollowersResponseDto getFollowers(String userId, int page, int size) {
        userRepository.findByUsername(userId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_USER));

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Follow> followPage = followRepository.findAllByFollowing_Username(pageable, userId);

        List<ProfileResponseDto> followers = followPage.stream()
                .map(follow -> ProfileResponseDto.builder()
                        .id(follow.getFollowerUsername())
                        .name(follow.getFollowerName())
                        .profileUrl(follow.getFollowerProfileUrl())
                        .job(follow.getFollowerJob())
                        .build())
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(followPage.getTotalPages())
                .totalElement(followPage.getTotalElements())
                .build();

        return FollowersResponseDto.builder()
                .page(pageResponseDto)
                .followers(followers)
                .build();
    }

    public FollowingsResponseDto getFollowings(String userId, int page, int size) {
        userRepository.findByUsername(userId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_USER));

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Follow> followPage = followRepository.findAllByFollower_Username(pageable, userId);

        List<ProfileResponseDto> followers = followPage.stream()
                .map(follow -> ProfileResponseDto.builder()
                        .id(follow.getFollowingUsername())
                        .name(follow.getFollowingName())
                        .profileUrl(follow.getFollowingProfileUrl())
                        .job(follow.getFollowingJob())
                        .build())
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(followPage.getTotalPages())
                .totalElement(followPage.getTotalElements())
                .build();

        return FollowingsResponseDto.builder()
                .page(pageResponseDto)
                .followings(followers)
                .build();
    }
}
