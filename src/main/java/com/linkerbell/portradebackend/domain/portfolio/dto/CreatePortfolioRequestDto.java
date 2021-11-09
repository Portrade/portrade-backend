package com.linkerbell.portradebackend.domain.portfolio.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePortfolioRequestDto {

    @NotEmpty(message = "NULL_PORTFOLIO_TITLE")
    private String title;

    @Size(min = 1, max = 500, message = "INVALID_PORTFOLIO_DESCRIPTION")
    @NotNull(message = "NULL_PORTFOLIO_DESCRIPTION")
    private String description;

    @NotEmpty(message = "NULL_PORTFOLIO_CATEGORY")
    private String category;

    @NotNull(message = "NULL_PORTFOLIO_ISPUBLIC")
    private boolean isPublic;

    @NotNull(message = "NULL_PORTFOLIO_MAINIMAGE")
    private MultipartFile mainImageFile;

    @NotNull(message = "NULL_PORTFOLIO_CONTENTFILES")
    private List<MultipartFile> contentFiles;

    @Builder
    public CreatePortfolioRequestDto(String title, String description, String category, boolean isPublic, MultipartFile mainImageFile, List<MultipartFile> contentFiles) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.isPublic = isPublic;
        this.mainImageFile = mainImageFile;
        this.contentFiles = contentFiles;
    }
}
