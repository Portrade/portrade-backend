package com.linkerbell.portradebackend.domain.portfolio.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Likes;
import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.repository.LikesRepository;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public boolean likePortfolio(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_PORTFOLIO));

        Optional<Likes> result = likesRepository.findByPortfolio_IdAndUser_Username(portfolioId, user.getUsername());
        if (result.isPresent()) {
            likesRepository.delete(result.get());
            return false;
        } else {
            Likes like = Likes.builder()
                    .user(user)
                    .portfolio(portfolio)
                    .build();
            likesRepository.save(like);
            return true;
        }
    }
}
