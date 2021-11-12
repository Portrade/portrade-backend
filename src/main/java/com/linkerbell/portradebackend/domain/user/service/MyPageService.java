package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.*;
import com.linkerbell.portradebackend.domain.user.repository.FollowRepository;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.common.File;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PortfolioRepository portfolioRepository;
    private final S3Util s3Util;

    @Transactional
    public ProfileImageResponseDto uploadProfileImage(User user, MultipartFile file) throws IOException {
        File uploadedProfileImage = s3Util.upload(file);

        user.getProfile().updateProfileUrl(uploadedProfileImage.getUrl());
        userRepository.save(user);

        return ProfileImageResponseDto.builder()
                .fileName(uploadedProfileImage.getFileName())
                .url(uploadedProfileImage.getUrl())
                .build();
    }

    public UserPortfoliosResponseDto getUserPortfolios(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<Portfolio> portfolios = portfolioRepository.findAllByUsername(pageable, userId);
        List<UserPortfolioResponseDto> userPortfolioResponseDtos = portfolios
                .stream()
                .map(portfolio -> new UserPortfolioResponseDto(portfolio.getId(), portfolio.getTitle(), portfolio.getCreatedDate()))
                .collect(Collectors.toList());

        return UserPortfoliosResponseDto.builder()
                .portfolios(userPortfolioResponseDtos)
                .maxPage(portfolios.getTotalPages())
                .build();
    }

    public ProfileResponseDto getUserProfile(String userId) {
        User user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_USER));
        return ProfileResponseDto.builder()
                .id(user.getUsername())
                .name(user.getName())
                .profileUrl(user.getUserProfileUrl())
                .job(user.getUserJob())
                .build();
    }

    @Transactional
    public void updateProfile(ProfileRequestDto profileRequestDto, User user) {
        User updateUser = profileRequestDto.toEntity(user);
        userRepository.save(updateUser);
    }

    public InsightResponseDto getMyInsight(User user) {
        int viewCount = 0;
        int likeCount = 0;
        int commentCount = 0;
        List<Portfolio> portfolios = portfolioRepository.findAllByUsername(user.getUsername());
        for (Portfolio portfolio : portfolios) {
            viewCount += portfolio.getViewCount();
            likeCount += portfolio.getLikeCount();
            commentCount += portfolio.getCommentCount();
        }

        int followingCount = followRepository.countByFollowing_Username(user.getUsername());
        int followerCount = followRepository.countByFollower_Username(user.getUsername());

        return InsightResponseDto.builder()
                .viewCount(viewCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .followingCount(followingCount)
                .followerCount(followerCount)
                .build();
    }

    @Transactional
    public void updateProfileJob(JobRequestDto jobRequestDto, User user) {
        user.updateJob(jobRequestDto.getJob());
        userRepository.save(user);
    }
}
