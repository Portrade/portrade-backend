package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class FileHandlingException extends RuntimeException {

    private final ErrorCode errorCode;

    public FileHandlingException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
