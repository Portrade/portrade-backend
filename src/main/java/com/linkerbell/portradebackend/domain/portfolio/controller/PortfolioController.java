package com.linkerbell.portradebackend.domain.portfolio.controller;

import com.linkerbell.portradebackend.domain.portfolio.dto.CreatePortfolioRequestDto;
import com.linkerbell.portradebackend.domain.portfolio.dto.PortfolioDetailResponseDto;
import com.linkerbell.portradebackend.domain.portfolio.service.PortfolioService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "포트폴리오 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Operation(summary = "포트폴리오 등록", description = "포트폴리오를 등록한다.")
    @PostMapping
    public ResponseEntity<CreateResponseDto> createPortfolioApi(
            @ModelAttribute CreatePortfolioRequestDto createPortfolioRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        CreateResponseDto createResponseDto = portfolioService.createPortfolio(createPortfolioRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponseDto);
    }

//    @Operation(summary = "포트폴리오 목록 조회", description = "포트폴리오 목록을 조회한다.")
//    @GetMapping
//    public ResponseEntity<PortfoliosResponseDto> getPortfoliosApi(
//            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
//            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size) {
//        PortfoliosResponseDto PortfoliosResponseDto = portfolioService.getPortfolios(page, size);
//        return ResponseEntity.status(HttpStatus.OK).body(PortfoliosResponseDto);
//    }

    @Operation(summary = "포트폴리오 상세 조회", description = "포트폴리오를 상세 조회한다.")
    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioDetailResponseDto> getPortfolioDetailApi(
            @Parameter(description = "상세 조회할 포트폴리오 ID") @PathVariable Long portfolioId) {
        PortfolioDetailResponseDto portfolioDetailResponseDto = portfolioService.getPortfolio(portfolioId);
        return ResponseEntity.status(HttpStatus.OK).body(portfolioDetailResponseDto);
    }

//    @Operation(summary = "포트폴리오 수정", description = "포트폴리오를 수정한다.")
//    @PutMapping("/{portfolioId}")
//    public ResponseEntity<Void> editPortfolioApi(
//            @Parameter(description = "수정할 포트폴리오 ID") @PathVariable Long portfolioId,
//            @RequestBody @Valid PortfolioRequestDto PortfolioRequestDto) {
//        portfolioService.updatePortfolio(portfolioId, PortfolioRequestDto);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

    @Operation(summary = "포트폴리오 삭제", description = "포트폴리오를 삭제한다.")
    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolioApi(
            @Parameter(description = "삭제할 포트폴리오 ID") @PathVariable Long portfolioId,
            @Parameter(hidden = true) @CurrentUser User user) {
        portfolioService.deletePortfolio(portfolioId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
