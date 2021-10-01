package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class FileUploadException extends RuntimeException {
    private ErrorCode errorCode;

    public FileUploadException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
