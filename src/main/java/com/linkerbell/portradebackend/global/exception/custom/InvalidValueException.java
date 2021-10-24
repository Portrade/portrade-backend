package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidValueException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidValueException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
