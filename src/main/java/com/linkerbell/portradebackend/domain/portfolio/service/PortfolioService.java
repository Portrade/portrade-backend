package com.linkerbell.portradebackend.domain.portfolio.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.dto.CreatePortfolioRequestDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfolioDetailResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfolioResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.File;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthenticatedException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import com.linkerbell.portradebackend.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public PortfoliosResponseDto getRecommendedPortfolios(int page, int size) {
        return null;
    }

    public PortfoliosResponseDto getRecentPortfolios(int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Portfolio> portfolioPage = portfolioRepository.findAll(pageable);

        List<PortfolioResponseDto> portfolioResponseDtos = portfolioPage.stream()
                .map(PortfolioResponseDto::of)
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(portfolioPage.getTotalPages())
                .totalElement(portfolioPage.getTotalElements())
                .build();

        return PortfoliosResponseDto.builder()
                .page(pageResponseDto)
                .portfolios(portfolioResponseDtos)
                .build();
    }

    public PortfoliosResponseDto getPortfoliosBySpecificCategory(int page, int size, String category) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<Portfolio> portfolioPage;
        if (category.equals("all")) {
            portfolioPage = portfolioRepository.findAll(pageable);
        } else {
            portfolioPage = portfolioRepository.findAllByCategoryEqualsIgnoreCase(pageable, category);
        }

        List<PortfolioResponseDto> portfolioResponseDtos = portfolioPage.stream()
                .map(PortfolioResponseDto::of)
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(portfolioPage.getTotalPages())
                .totalElement(portfolioPage.getTotalElements())
                .build();

        return PortfoliosResponseDto.builder()
                .page(pageResponseDto)
                .portfolios(portfolioResponseDtos)
                .build();
    }

    public PortfoliosResponseDto getPortfoliosApi(int page, int size, String keyword, String sort, String direction) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Portfolio> portfolioPage;
        switch (sort) {
            case "created":
                if (direction.equals("asc")) {
                    portfolioPage = portfolioRepository.findAllByTitleContainingIgnoreCaseOrderByIdAsc(pageable, keyword);
                } else {
                    portfolioPage = portfolioRepository.findAllByTitleContainingIgnoreCaseOrderByIdDesc(pageable, keyword);
                }
                break;
            case "view":
                if (direction.equals("asc")) {
                    portfolioPage = portfolioRepository.findAllByTitleContainingIgnoreCaseOrderByViewCountAsc(pageable, keyword);
                } else {
                    portfolioPage = portfolioRepository.findAllByTitleContainingIgnoreCaseOrderByViewCountDesc(pageable, keyword);
                }
                break;
            case "dictionary":
                if (direction.equals("asc")) {
                    portfolioPage = portfolioRepository.findAllByTitleContainingIgnoreCaseOrderByTitleAsc(pageable, keyword);
                } else {
                    portfolioPage = portfolioRepository.findAllByTitleContainingIgnoreCaseOrderByTitleDesc(pageable, keyword);
                }
                break;
            default:
                portfolioPage = portfolioRepository.findAllByTitleContainingIgnoreCaseOrderByIdDesc(pageable, keyword);
                break;
        }

        List<PortfolioResponseDto> portfolioResponseDtos = portfolioPage.stream()
                .map(PortfolioResponseDto::of)
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(portfolioPage.getTotalPages())
                .totalElement(portfolioPage.getTotalElements())
                .build();

        return PortfoliosResponseDto.builder()
                .page(pageResponseDto)
                .portfolios(portfolioResponseDtos)
                .build();
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
