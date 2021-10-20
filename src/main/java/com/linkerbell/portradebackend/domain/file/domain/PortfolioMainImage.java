package com.linkerbell.portradebackend.domain.file.domain;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = "portfolio")
@DiscriminatorValue("PORTFOLIO_MAIN_IMAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioMainImage extends File {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id_main")
    private Portfolio portfolio;

    @Builder
    private PortfolioMainImage(Long id, String url, String fileName, String originalFileName, String extension, Portfolio portfolio) {
        super(id, url, fileName, originalFileName, extension);
        this.portfolio = portfolio;
    }

    public static PortfolioMainImage of(UploadResponseDto uploadResponseDto, Portfolio portfolio) {
        return PortfolioMainImage.builder()
                .url(uploadResponseDto.getUrl())
                .fileName(uploadResponseDto.getNewFileName())
                .originalFileName(uploadResponseDto.getOriginalFileName())
                .extension(uploadResponseDto.getExtension())
                .portfolio(portfolio)
                .build();
    }
}
