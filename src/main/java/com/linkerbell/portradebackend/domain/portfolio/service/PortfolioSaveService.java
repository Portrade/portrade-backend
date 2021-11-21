package com.linkerbell.portradebackend.domain.portfolio.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.domain.PortfolioSave;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfolioResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioSaveRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
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
public class PortfolioSaveService {

    private final PortfolioSaveRepository portfolioSaveRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public boolean savePortfolio(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_PORTFOLIO));

        Optional<PortfolioSave> result = portfolioSaveRepository.findByPortfolio_IdAndUser_Username(portfolioId, user.getUsername());
        if (result.isPresent()) {
            portfolioSaveRepository.delete(result.get());
            return false;
        } else {
            PortfolioSave portfolioSave = PortfolioSave.builder()
                    .user(user)
                    .portfolio(portfolio)
                    .build();
            portfolioSaveRepository.save(portfolioSave);
            return true;
        }
    }

    public PortfoliosResponseDto getSavedPortfolios(int page, int size, User user) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<PortfolioSave> portfolioSavePage = portfolioSaveRepository.findAllByUser(pageable, user);

        List<PortfolioResponseDto> portfolioResponseDtos = portfolioSavePage.stream()
                .map(PortfolioResponseDto::of)
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(portfolioSavePage.getTotalPages())
                .totalElement(portfolioSavePage.getTotalElements())
                .build();

        return PortfoliosResponseDto.builder()
                .page(pageResponseDto)
                .portfolios(portfolioResponseDtos)
                .build();
    }
}
