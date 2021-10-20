package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.*;
import com.linkerbell.portradebackend.domain.user.repository.FollowRepository;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NotExistException;
import com.linkerbell.portradebackend.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserMyPageService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final FollowRepository followRepository;
    private final PortfolioRepository portfolioRepository;
    private final S3Util s3Util;

    @Transactional
    public ProfileImageResponseDto uploadProfileImage(User user, MultipartFile file) throws IOException {
        UploadResponseDto uploadResponseDto = s3Util.upload(file);

        user.getProfile().updateProfileUrl(uploadResponseDto.getUrl());
        userRepository.save(user);

        return ProfileImageResponseDto.builder()
                .fileName(uploadResponseDto.getNewFileName())
                .url(uploadResponseDto.getUrl())
                .build();
    }

    public UserPortfoliosResponseDto getUserPortfolios(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(
                page-1,
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

    public ProfileResponeDto getUserProfile(String userId) {
        User user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new NotExistException(ErrorCode.NONEXISTENT_USER));
        return ProfileResponeDto.builder()
                .id(user.getId())
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
        //포트폴리오 총 조회수, 총 좋아요 수, 총 댓글 수, 팔로우 수, 팔로워 수
        List<Portfolio> portfolios = portfolioRepository.findAllByUsername(user.getUsername());

        return InsightResponseDto.builder()
                .viewCount(getViewCount(portfolios))
                .likes(getLikes(portfolios))
                .comment(getComments(portfolios))
                .followers(getFollowers(user.getId()))
                .followings(getFollowings(user.getId()))
                .build();
    }

    @Transactional
    public void updateProfileJob(JobRequestDto jobRequestDto, User user) {
        user.updateJob(jobRequestDto.getJob());
        userRepository.save(user);
    }

    private Integer getViewCount(List<Portfolio> portfolios) {
        return portfolios
                .stream()
                .map(portfolio -> portfolio.getViewCount())
                .reduce(0, Integer::sum);
    }

    private Integer getLikes(List<Portfolio> portfolios) {
        return portfolios
                .stream()
                .map(portfolio -> portfolio.getLikes().size())
                .reduce(0, Integer::sum);
    }

    private Integer getComments(List<Portfolio> portfolios) {
        return portfolios
                .stream()
                .map(portfolio -> portfolio.getComments().size())
                .reduce(0, Integer::sum);
    }

    //나를 팔로윙 하는 거를 찾는거니깐 내 아이디가 followings 에 있어야 함
    private Long getFollowers(UUID id) {
        return followRepository.countByFollowing_Id(id);
    }

    //내가 팔로윙 하는 거를 찾는거니깐 내 아이디가 followers 에 있어야 함
    private Long getFollowings(UUID id) {
        return followRepository.countByFollower_Id(id);
    }
}
