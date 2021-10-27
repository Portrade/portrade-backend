package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicatedValueException extends RuntimeException {

    private final ErrorCode errorCode;

    public DuplicatedValueException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}