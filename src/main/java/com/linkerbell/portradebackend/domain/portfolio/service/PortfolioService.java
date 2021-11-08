package com.linkerbell.portradebackend.domain.portfolio.service;

import com.linkerbell.portradebackend.domain.file.domain.PortfolioContentFile;
import com.linkerbell.portradebackend.domain.file.domain.PortfolioMainImage;
import com.linkerbell.portradebackend.domain.file.dto.FileResponseDto;
import com.linkerbell.portradebackend.domain.file.service.FileService;
import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.dto.CreatePortfolioRequestDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfolioDetailResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthenticatedException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final FileService fileService;

    private void checkUserPermission(Portfolio portfolio, User user) {
        if (Objects.isNull(user)) {
            throw new UnAuthenticatedException(ErrorCode.NONEXISTENT_AUTHENTICATION);
        } else if (!user.equals(portfolio.getCreator()) && !user.isAdmin()) {
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);
        }
    }

    @Transactional
    public IdResponseDto createPortfolio(CreatePortfolioRequestDto createPortfolioRequestDto, User user) {
        Portfolio portfolio = Portfolio.builder()
                .creator(user)
                .title(createPortfolioRequestDto.getTitle())
                .description(createPortfolioRequestDto.getDescription())
                .category(createPortfolioRequestDto.getCategory())
                .isPublic(createPortfolioRequestDto.isPublic())
                .build();
        portfolioRepository.save(portfolio);

        if (Objects.nonNull(createPortfolioRequestDto.getMainImage())) {
            fileService.createFile(PortfolioMainImage.class, portfolio, createPortfolioRequestDto.getMainImage());
        }
        for (MultipartFile contentFile : createPortfolioRequestDto.getContentFiles()) {
            fileService.createFile(PortfolioContentFile.class, portfolio, contentFile);
        }

        return new IdResponseDto(portfolio.getId());
    }

    @Transactional
    public PortfolioDetailResponseDto getPortfolio(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_PORTFOLIO));

        if (!portfolio.isPublic()) {
            checkUserPermission(portfolio, user);
        }

        portfolio.addViewCount();

        FileResponseDto mainImage = Objects.isNull(portfolio.getMainImage())
                ? null
                : FileResponseDto.of(portfolio.getMainImage());
        List<FileResponseDto> contentFiles = portfolio.getContentFiles()
                .stream()
                .map(FileResponseDto::of)
                .collect(Collectors.toList());

        return PortfolioDetailResponseDto.builder()
                .id(portfolio.getId())
                .creator(portfolio.getCreator().getUsername())
                .title(portfolio.getTitle())
                .description(portfolio.getDescription())
                .category(portfolio.getCategory())
                .isPublic(portfolio.isPublic())
                .viewCount(portfolio.getViewCount())
                .likeCount(portfolio.getLikeCount())
                .commentCount(portfolio.getCommentCount())
                .mainImage(mainImage)
                .contentFiles(contentFiles)
                .createdDate(portfolio.getCreatedDate())
                .lastModifiedDate(portfolio.getLastModifiedDate())
                .build();
    }

    @Transactional
    public void deletePortfolio(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_PORTFOLIO));

        checkUserPermission(portfolio, user);

        portfolioRepository.delete(portfolio);
    }
}
