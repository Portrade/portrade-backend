package com.linkerbell.portradebackend.global.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadResponseDto {

    @NotNull
    private String originalFileName;

    @NotNull
    private String newFileName;

    @NotNull
    private String url;

    @Builder
    public UploadResponseDto(String originalFileName, String newFileName, String url) {
        this.originalFileName = originalFileName;
        this.newFileName = newFileName;
        this.url = url;
    }
}
