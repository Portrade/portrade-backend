package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotExsitException extends RuntimeException{
    private ErrorCode errorCode;

    public NotExsitException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
