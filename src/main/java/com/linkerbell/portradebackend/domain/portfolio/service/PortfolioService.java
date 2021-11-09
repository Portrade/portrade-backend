package com.linkerbell.portradebackend.domain.portfolio.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.dto.CreatePortfolioRequestDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfolioDetailResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.File;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthenticatedException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import com.linkerbell.portradebackend.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final S3Util s3Util;

    private void checkUserPermission(Portfolio portfolio, User user) {
        if (Objects.isNull(user)) {
            throw new UnAuthenticatedException(ErrorCode.NONEXISTENT_AUTHENTICATION);
        } else if (!user.equals(portfolio.getCreator()) && !user.isAdmin()) {
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORIZATION);
        }
    }

    @Transactional
    public IdResponseDto createPortfolio(CreatePortfolioRequestDto createPortfolioRequestDto, User user) {
        File mainImageFile = s3Util.upload(createPortfolioRequestDto.getMainImageFile());
        List<File> contentFiles = createPortfolioRequestDto.getContentFiles()
                .stream()
                .map(s3Util::upload)
                .collect(Collectors.toList());

        Portfolio portfolio = Portfolio.builder()
                .creator(user)
                .title(createPortfolioRequestDto.getTitle())
                .description(createPortfolioRequestDto.getDescription())
                .category(createPortfolioRequestDto.getCategory())
                .isPublic(createPortfolioRequestDto.isPublic())
                .mainImageFile(mainImageFile)
                .contentFiles(contentFiles)
                .build();
        portfolioRepository.save(portfolio);

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

        return PortfolioDetailResponseDto.of(portfolio);
    }

    @Transactional
    public void deletePortfolio(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_PORTFOLIO));

        checkUserPermission(portfolio, user);

        portfolioRepository.delete(portfolio);
    }
}
