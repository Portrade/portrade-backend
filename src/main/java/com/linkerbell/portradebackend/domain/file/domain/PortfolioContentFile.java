package com.linkerbell.portradebackend.domain.file.domain;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = "portfolio")
@DiscriminatorValue("PORTFOLIO_CONTENT_FILE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioContentFile extends File {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id_content")
    private Portfolio portfolio;

    @Builder
    private PortfolioContentFile(Long id, String url, String fileName, String originalFileName, String extension, Portfolio portfolio) {
        super(id, url, fileName, originalFileName, extension);
        this.portfolio = portfolio;
    }

    public static PortfolioContentFile of(UploadResponseDto uploadResponseDto, Portfolio portfolio) {
        return PortfolioContentFile.builder()
                .url(uploadResponseDto.getUrl())
                .fileName(uploadResponseDto.getNewFileName())
                .originalFileName(uploadResponseDto.getOriginalFileName())
                .extension(uploadResponseDto.getExtension())
                .portfolio(portfolio)
                .build();
    }
}
