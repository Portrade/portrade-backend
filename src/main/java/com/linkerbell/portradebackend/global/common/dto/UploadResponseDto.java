package com.linkerbell.portradebackend.global.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadResponseDto {

    private String originalFileName;
    private String newFileName;
    private String url;

    @Builder
    public UploadResponseDto(String originalFileName, String newFileName, String url) {
        this.originalFileName = originalFileName;
        this.newFileName = newFileName;
        this.url = url;
    }
}
