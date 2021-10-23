package com.linkerbell.portradebackend.domain.file.dto;

import com.linkerbell.portradebackend.domain.file.domain.File;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FileDetailResponseDto {

    private final Long id;
    private final String url;
    private final String fileName;
    private final String originalFileName;
    private final String extension;

    @Builder
    private FileDetailResponseDto(Long id, String url, String fileName, String originalFileName, String extension) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.extension = extension;
    }

    public static FileDetailResponseDto of(File file) {
        return FileDetailResponseDto.builder()
                .id(file.getId())
                .url(file.getUrl())
                .fileName(file.getFileName())
                .originalFileName(file.getOriginalFileName())
                .extension(file.getExtension())
                .build();
    }
}
