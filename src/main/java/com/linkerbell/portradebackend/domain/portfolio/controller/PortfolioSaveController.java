package com.linkerbell.portradebackend.domain.portfolio.controller;

import com.linkerbell.portradebackend.domain.portfolio.dto.PortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.service.PortfolioSaveService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "포트폴리오 저장 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class PortfolioSaveController {

    private final PortfolioSaveService portfolioSaveService;

    @Operation(summary = "포트폴리오 저장/취소", description = "해당 포트폴리오를 저장 혹은 저장 취소한다.")
    @PatchMapping("/{portfolioId}/save")
    public ResponseEntity<Void> portfolioSaveApi(
            @Parameter(description = "저장할 포트폴리오 ID") @PathVariable("portfolioId") Long portfolioId,
            @Parameter(hidden = true) @CurrentUser User user) {
        boolean isSaved = portfolioSaveService.savePortfolio(portfolioId, user);
        if (isSaved) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Operation(summary = "저장된 포트폴리오 목록 조회", description = "저장된 포트폴리오 목록을 조회한다.")
    @GetMapping("/save")
    public ResponseEntity<PortfoliosResponseDto> getSavedPortfoliosApi(
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "9") int size,
            @Parameter(hidden = true) @CurrentUser User user) {
        PortfoliosResponseDto portfoliosResponseDto = portfolioSaveService.getSavedPortfolios(page, size, user);
        return ResponseEntity.status(HttpStatus.OK).body(portfoliosResponseDto);
    }
}
