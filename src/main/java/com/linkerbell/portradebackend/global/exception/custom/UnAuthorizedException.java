package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UnAuthorizedException extends RuntimeException {

    private final ErrorCode errorCode;

    public UnAuthorizedException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
