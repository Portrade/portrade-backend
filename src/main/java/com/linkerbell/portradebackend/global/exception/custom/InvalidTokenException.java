package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException {
    private ErrorCode errorCode;

    public InvalidTokenException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
