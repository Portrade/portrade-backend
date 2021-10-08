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
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NotExistException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final FileService fileService;

    @Transactional
    public CreateResponseDto createPortfolio(CreatePortfolioRequestDto createPortfolioRequestDto, User user) {
        Portfolio portfolio = Portfolio.builder()
                .creator(user)
                .title(createPortfolioRequestDto.getTitle())
                .description(createPortfolioRequestDto.getDescription())
                .category(createPortfolioRequestDto.getCategory())
                .isPublic(createPortfolioRequestDto.isPublic())
                .build();
        portfolioRepository.save(portfolio);

        fileService.createFile(PortfolioMainImage.class, portfolio, createPortfolioRequestDto.getMainImage());
        for (MultipartFile contentFile : createPortfolioRequestDto.getContentFiles()) {
            fileService.createFile(PortfolioContentFile.class, portfolio, contentFile);
        }

        return new CreateResponseDto(portfolio.getId());
    }

    @Transactional
    public PortfolioDetailResponseDto getPortfolio(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NotExistException(ErrorCode.NONEXISTENT_PORTFOLIO_ID));

        portfolio.addViewCount();

        FileResponseDto mainImage = FileResponseDto.of(portfolio.getMainImage());
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
// TODO         .likeCount(portfolio.getLikeCount())
// TODO         .commentCount(portfolio.getCommentCount())
                .mainImage(mainImage)
                .contentFiles(contentFiles)
                .createdDate(portfolio.getCreatedDate())
                .lastModifiedDate(portfolio.getLastModifiedDate())
                .build();
    }

    @Transactional
    public void deletePortfolio(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NotExistException(ErrorCode.NONEXISTENT_PORTFOLIO_ID));

        if (user.equals(portfolio.getCreator()) || user.isAdmin()) {
            portfolioRepository.delete(portfolio);
        } else {
            throw new UnAuthorizedException(ErrorCode.NONEXISTENT_AUTHORITY);
        }
    }
}
