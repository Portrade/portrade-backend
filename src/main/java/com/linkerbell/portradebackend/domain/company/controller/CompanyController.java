package com.linkerbell.portradebackend.domain.company.controller;

import com.linkerbell.portradebackend.domain.company.dto.CompanyDetailResponseDto;
import com.linkerbell.portradebackend.domain.company.dto.RecruitmentsResponseDto;
import com.linkerbell.portradebackend.domain.company.dto.CompanyRequestDto;
import com.linkerbell.portradebackend.domain.company.dto.CreateCompanyRequestDto;
import com.linkerbell.portradebackend.domain.company.service.CompanyService;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.annotation.CurrentUser;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "기업 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "기업 등록", description = "기업 정보를 등록한다.")
    @PostMapping
    public ResponseEntity<IdResponseDto> createCompanyApi(
            @RequestBody @Valid CreateCompanyRequestDto companyRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        IdResponseDto idResponseDto = companyService.createCompany(companyRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(idResponseDto);
    }

    @Operation(summary = "기업 상세 조회", description = "기업 정보를 상세 조회한다.")
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyDetailResponseDto> getCompanyDetailApi(
            @Parameter(description = "상세 조회할 기업 ID") @PathVariable Long companyId) {
        CompanyDetailResponseDto companyDetailResponseDto = companyService.getCompany(companyId);
        return ResponseEntity.status(HttpStatus.OK).body(companyDetailResponseDto);
    }

    @Operation(summary = "기업 수정", description = "기업 정보 수정한다.")
    @PutMapping("/{companyId}")
    public ResponseEntity<IdResponseDto> editCompanyApi(
            @Parameter(description = "수정할 기업 ID") @PathVariable Long companyId,
            @RequestBody CompanyRequestDto companyRequestDto,
            @Parameter(hidden = true) @CurrentUser User user) {
        IdResponseDto idResponseDto = companyService.updateCompany(companyRequestDto, companyId, user);
        return ResponseEntity.status(HttpStatus.OK).body(idResponseDto);
    }

    @Operation(summary = "기업의 모든 공고 조회", description = "기업의 모든 공고를 조회한다.")
    @GetMapping("/{companyId}/recruitments")
    public ResponseEntity<RecruitmentsResponseDto> getRecruitmentsApi(
            @Parameter(description = "조회할 기업 ID") @PathVariable Long companyId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "6") int size) {
        RecruitmentsResponseDto recruitmentsResponseDto = companyService.getRecruitments(page, size, companyId);
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentsResponseDto);
    }

    @Operation(summary = "기업 삭제", description = "기업을 삭제한다.")
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompanyApi(
            @Parameter(description = "삭제할 기업 ID") @PathVariable Long companyId,
            @Parameter(hidden = true) @CurrentUser User user) {
        companyService.deleteCompany(companyId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
