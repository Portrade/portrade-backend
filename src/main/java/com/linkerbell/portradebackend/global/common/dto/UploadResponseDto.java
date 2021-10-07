package com.linkerbell.portradebackend.global.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadResponseDto {

    private String url;
    private String newFileName;
    private String originalFileName;
    private String extension;

    @Builder
    public UploadResponseDto(String url, String newFileName, String originalFileName, String extension) {
        this.url = url;
        this.newFileName = newFileName;
        this.originalFileName = originalFileName;
        this.extension = extension;
    }
}
