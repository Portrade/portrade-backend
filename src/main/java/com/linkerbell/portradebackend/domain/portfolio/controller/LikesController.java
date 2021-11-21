package com.linkerbell.portradebackend.domain.portfolio.controller;

import com.linkerbell.portradebackend.domain.portfolio.service.LikesService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "포트폴리오 좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class LikesController {

    private final LikesService likesService;

    @Operation(summary = "포트폴리오 좋아요/취소")
    @PatchMapping("/{portfolioId}/like")
    public ResponseEntity<Void> portfolioLikeApi(
            @Parameter(description = "좋아요할 포트폴리오 ID") @PathVariable("portfolioId") Long portfolioId,
            @Parameter(hidden = true) @CurrentUser User user) {
        boolean isLiked = likesService.likePortfolio(portfolioId, user);
        if (isLiked) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
