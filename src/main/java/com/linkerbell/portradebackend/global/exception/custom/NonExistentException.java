package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NonExistentException extends RuntimeException {

    private final ErrorCode errorCode;

    public NonExistentException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
