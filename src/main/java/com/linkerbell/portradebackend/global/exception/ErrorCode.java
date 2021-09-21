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
    INVALID_USER_ID( "M001", "아이디는 영문 소문자, 숫자만 입력하세요."),
    NULL_USER_ID("M002","아이디는 필수 입니다."),
    INVALID_USER_NAME("M003", "이름을 2~8자 사이로 입력해주세요."),
    NULL_USER_NAME("M004","이름은 필수 입니다."),
    INVALID_USER_PASSWORD("M005","비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함 8글자 이상이어야 합니다."),
    NULL_USER_PASSWORD("M006", "비밀번호는 필수 입니다."),
    NULL_USER_CATEGORY("M007",  "희망직무는 필수 입니다."),
    INVALID_SIZE_USER_BIRTHDATE("M008", "생일은 8 글자로 입력해주세요."),
    NULL_USER_BIRTHDATE("M009", "생일은 필수 입니다."),
    ;

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
