package com.linkerbell.portradebackend.domain.save.service;

import com.linkerbell.portradebackend.domain.save.domain.UserPortfolio;
import com.linkerbell.portradebackend.domain.save.domain.UserRecruitment;
import com.linkerbell.portradebackend.domain.save.dto.SavedPortfolioResponseDto;
import com.linkerbell.portradebackend.domain.save.dto.SavedPortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.save.dto.SavedRecruitmentResponseDto;
import com.linkerbell.portradebackend.domain.save.dto.SavedRecruitmentsResponseDto;
import com.linkerbell.portradebackend.domain.save.repository.UserRecruitmentRepository;
import com.linkerbell.portradebackend.domain.save.repository.UserPortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SaveService {

    private final UserPortfolioRepository userPortfolioRepository;
    private final UserRecruitmentRepository userRecruitmentRepository;

    public SavedPortfoliosResponseDto getSavedPortfolios(int page, int size, User user) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<UserPortfolio> userPortfolios = userPortfolioRepository.findByUser_Id(pageable, user.getId());
        List<SavedPortfolioResponseDto> savedPortfolioResponseDtos = userPortfolios.stream()
                .map(userPortfolio -> userPortfolio.getPortfolio())
                .map(portfolio -> SavedPortfolioResponseDto.builder()
                        .id(portfolio.getId())
                        .title(portfolio.getTitle())
                        .createdDate(portfolio.getCreatedDate())
                        .build())
                .collect(Collectors.toList());

        return SavedPortfoliosResponseDto.builder()
                .portfolios(savedPortfolioResponseDtos)
                .maxPage(userPortfolios.getTotalPages())
                .build();
    }

    public SavedRecruitmentsResponseDto getSavedRecruitments(int page, int size, User user) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<UserRecruitment> userRecruitments = userRecruitmentRepository.findByUser_Id(pageable, user.getId());
        List<SavedRecruitmentResponseDto> savedRecruitmentsResponseDto = userRecruitments.stream()
                .map(userRecruitment -> userRecruitment.getRecruitment())
                .map(recruitment -> SavedRecruitmentResponseDto.builder()
                        .logo(recruitment.getLogo())
                        .title(recruitment.getTitle())
                        .companyName(recruitment.getCompany().getName())
                        .career(recruitment.getCareer())
                        .education(recruitment.getEducation())
                        .address(recruitment.getAddress())
                        .lastModifiedDate(recruitment.getLastModifiedDate())
                        .build())
                .collect(Collectors.toList());

        return SavedRecruitmentsResponseDto
                .builder()
                .recruitments(savedRecruitmentsResponseDto)
                .maxPage(userRecruitments.getTotalPages())
                .build();
    }
}
