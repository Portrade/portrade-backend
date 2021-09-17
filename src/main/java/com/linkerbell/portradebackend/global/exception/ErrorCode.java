package com.linkerbell.portradebackend.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /**
     * C: 공통 에러
     */


    /**
     * M: 회원 관련 에러
     */
    INVALID_USER_EMAIL( "M001", "이메일 형식이 올바르지 않습니다."),
    ;

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
