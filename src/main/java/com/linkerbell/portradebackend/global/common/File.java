package com.linkerbell.portradebackend.global.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;


@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    private String url;
    private String fileName;
    private String extension;

    @Builder
    public File(String url, String fileName, String extension) {
        this.url = url;
        this.fileName = fileName;
        this.extension = extension;
    }
}
