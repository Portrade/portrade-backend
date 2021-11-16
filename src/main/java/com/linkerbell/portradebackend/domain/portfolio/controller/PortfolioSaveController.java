package com.linkerbell.portradebackend.domain.portfolio.controller;

import com.linkerbell.portradebackend.domain.portfolio.service.PortfolioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "포트폴리오 저장 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class PortfolioSaveController {

    private final PortfolioService portfolioService;
}
