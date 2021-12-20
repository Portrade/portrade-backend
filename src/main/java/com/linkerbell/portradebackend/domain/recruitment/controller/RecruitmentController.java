package com.linkerbell.portradebackend.domain.recruitment.controller;

import com.linkerbell.portradebackend.domain.company.dto.RecruitmentsResponseDto;
import com.linkerbell.portradebackend.domain.recruitment.dto.RecruitmentDetailResponseDto;
import com.linkerbell.portradebackend.domain.recruitment.dto.RecruitmentRequestDto;
import com.linkerbell.portradebackend.domain.recruitment.service.RecruitmentService;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "기업 공고 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitments")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @Operation(summary = "기업 공고 등록", description = "기업 공고를 등록한다.")
    @PostMapping("/{companyId}")
    public ResponseEntity<IdResponseDto> createRecruitmentApi(
            @Parameter(description = "공고를 추가할 기업 ID") @PathVariable Long companyId,
            @RequestBody @Valid RecruitmentRequestDto recruitmentRequestDto) {
        IdResponseDto idResponseDto = recruitmentService.createRecruitment(companyId, recruitmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(idResponseDto);
    }

    @Operation(summary = "기업 공고 목록 조회", description = "기업 공고 목록을 조회한다.")
    @GetMapping
    public ResponseEntity<RecruitmentsResponseDto> getRecruitmentsApi(
            @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "반환할 데이터 수") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "지역") @RequestParam(value="area", defaultValue = "") String area,
            @Parameter(description = "직종 ") @RequestParam(value="job", defaultValue = "") String job,
            @Parameter(description = "제목") @RequestParam(value="title", defaultValue = "") String title) {
        RecruitmentsResponseDto recruitmentsResponseDto = recruitmentService.getRecruitments(page, size, area, job, title);
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentsResponseDto);
    }

    @Operation(summary = "기업 공고 상세 조회", description = "기업 공고를 상세 조회한다.")
    @GetMapping("/{recruitmentId}")
    public ResponseEntity<RecruitmentDetailResponseDto> getRecruitmentDetailApi(
            @Parameter(description = "상세 조회할 기업 공고 ID") @PathVariable Long recruitmentId) {
        RecruitmentDetailResponseDto recruitmentDetailResponseDto = recruitmentService.getRecruitment(recruitmentId);
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentDetailResponseDto);
    }

    @Operation(summary = "기업 공고 수정", description = "기업 공고를 수정한다.")
    @PutMapping("/{recruitmentId}")
    public ResponseEntity<IdResponseDto> editRecruitmentApi(
            @Parameter(description = "수정할 기업 공고 ID") @PathVariable Long recruitmentId,
            @RequestBody @Valid RecruitmentRequestDto recruitmentRequestDto) {
        IdResponseDto idResponseDto = recruitmentService.editRecruitment(recruitmentId, recruitmentRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(idResponseDto);
    }

    @Operation(summary = "기업 공고 삭제", description = "기업 공고를 삭제한다.")
    @DeleteMapping("/{recruitmentId}")
    public ResponseEntity<Void> deleteRecruitmentApi(
            @Parameter(description = "삭제할 기업 공고 ID") @PathVariable Long recruitmentId) {
        recruitmentService.deleteRecruitment(recruitmentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
