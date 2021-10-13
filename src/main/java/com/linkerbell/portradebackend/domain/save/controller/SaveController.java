package com.linkerbell.portradebackend.domain.save.controller;

import com.linkerbell.portradebackend.domain.save.dto.SavedPortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.save.dto.SavedRecruitmentsResponseDto;
import com.linkerbell.portradebackend.domain.save.service.SaveService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "저장된 목록 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/save")
public class SaveController {

    private final SaveService saveService;

    @Operation(summary = "저장된 포트폴리오 목록 조회", description = "저장된 포트폴리오 목록을 조회한다.")
    @GetMapping("/portfolios")
    public ResponseEntity<SavedPortfoliosResponseDto> getSavedPortfoliosApi(
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(hidden = true) @CurrentUser User user){
        SavedPortfoliosResponseDto savedPortfoliosResponseDto = saveService.getSavedPortfolios(page, size, user);
        return ResponseEntity.status(HttpStatus.OK).body(savedPortfoliosResponseDto);
    }

    @Operation(summary = "저장된 기업 공고 목록 조회", description = "저장된 기업 공고 목록을 조회한다.")
    @GetMapping("/recruitments")
    public ResponseEntity<SavedRecruitmentsResponseDto> getSavedRecruitmentsApi(
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(hidden = true) @CurrentUser User user){
        SavedRecruitmentsResponseDto savedRecruitmentsResponseDto = saveService.getSavedRecruitments(page, size, user);
        return ResponseEntity.status(HttpStatus.OK).body(savedRecruitmentsResponseDto);
    }
}
