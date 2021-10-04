package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotExistException extends RuntimeException{
    private ErrorCode errorCode;

    public NotExistException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
