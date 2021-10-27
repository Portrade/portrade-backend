package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UnAuthenticatedException extends RuntimeException {

    private final ErrorCode errorCode;

    public UnAuthenticatedException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
