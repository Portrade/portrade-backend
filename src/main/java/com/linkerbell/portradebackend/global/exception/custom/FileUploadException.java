package com.linkerbell.portradebackend.global.exception.custom;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import lombok.Getter;

// TODO 클래스명 변경: 업로드 말고도 제거할 때도 사용
@Getter
public class FileUploadException extends RuntimeException {
    private ErrorCode errorCode;

    public FileUploadException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
