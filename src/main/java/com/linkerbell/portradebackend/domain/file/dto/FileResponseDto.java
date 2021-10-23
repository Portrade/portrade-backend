package com.linkerbell.portradebackend.domain.file.dto;

import com.linkerbell.portradebackend.domain.file.domain.File;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FileResponseDto {

    private final String url;
    private final String name;
    private final String extension;

    @Builder
    private FileResponseDto(String url, String name, String extension) {
        this.url = url;
        this.name = name;
        this.extension = extension;
    }

    public static FileResponseDto of(File file) {
        return FileResponseDto.builder()
                .url(file.getUrl())
                .name(file.getOriginalFileName())
                .extension(file.getExtension())
                .build();
    }
}
