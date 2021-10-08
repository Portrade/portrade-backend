package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotUniqueException extends RuntimeException{
    private ErrorCode errorCode;

    public NotUniqueException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
