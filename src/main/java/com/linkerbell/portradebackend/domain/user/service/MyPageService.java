package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfolioResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.InsightResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.JobRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.ProfileResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.FollowRepository;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.common.File;
import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
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
    public ProfileResponseDto uploadProfileImage(User user, MultipartFile file) {
        File uploadedProfileImage = s3Util.upload(file);

        user.getProfile().updateProfileImageFile(uploadedProfileImage);
        userRepository.save(user);

        return ProfileResponseDto.builder()
                .id(user.getUsername())
                .name(user.getName())
                .profileImageUrl(user.getUserProfileUrl())
                .job(user.getUserJob())
                .build();
    }

    public PortfoliosResponseDto getUserPortfolios(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Portfolio> portfolioPage = portfolioRepository.findAllByCreator_Username(pageable, userId);

        List<PortfolioResponseDto> portfolioResponseDtos = portfolioPage.stream()
                .map(portfolio -> PortfolioResponseDto.builder()
                        .id(portfolio.getId())
                        .creator(portfolio.getCreator().getUsername())
                        .title(portfolio.getTitle())
                        .mainImageUrl(portfolio.getMainImageFile().getUrl())
                        .createdDate(portfolio.getCreatedDate())
                        .lastModifiedDate(portfolio.getLastModifiedDate())
                        .build())
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(portfolioPage.getTotalPages())
                .totalElement(portfolioPage.getTotalElements())
                .build();

        return PortfoliosResponseDto.builder()
                .portfolios(portfolioResponseDtos)
                .page(pageResponseDto)
                .build();
    }

    public ProfileResponseDto getUserProfile(String userId) {
        User user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_USER));
        return ProfileResponseDto.builder()
                .id(user.getUsername())
                .name(user.getName())
                .profileImageUrl(user.getUserProfileUrl())
                .job(user.getUserJob())
                .build();
    }

    @Transactional
    public void updateProfile(ProfileRequestDto profileRequestDto, User user) {
        user.updateProfile(profileRequestDto.getName(),
                profileRequestDto.getBirthDate(),
                profileRequestDto.getCollege(),
                profileRequestDto.getWantedJob(),
                profileRequestDto.isGraduated());
        userRepository.save(user);
    }

    public InsightResponseDto getMyInsight(User user) {
        int viewCount = 0;
        int likeCount = 0;
        int commentCount = 0;
        int followingCount = followRepository.countByFollowing_Username(user.getUsername());
        int followerCount = followRepository.countByFollower_Username(user.getUsername());
        List<Portfolio> portfolios = portfolioRepository.findAllByCreator_Username(user.getUsername());
        for (Portfolio portfolio : portfolios) {
            viewCount += portfolio.getViewCount();
            likeCount += portfolio.getLikeCount();
            commentCount += portfolio.getCommentCount();
        }

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
