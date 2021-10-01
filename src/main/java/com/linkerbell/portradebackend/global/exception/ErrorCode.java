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
    INVALID_USER_ID("M001", "아이디는 영문 소문자, 숫자만 입력하세요."),
    NULL_USER_ID("M002", "아이디는 필수 입니다."),
    INVALID_USER_NAME("M003", "이름을 2~8자 사이로 입력해주세요."),
    NULL_USER_NAME("M004", "이름은 필수 입니다."),
    INVALID_USER_PASSWORD("M005", "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함 8글자 이상이어야 합니다."),
    NULL_USER_PASSWORD("M006", "비밀번호는 필수 입니다."),
    NULL_USER_WANTEDJOB("M007", "희망직무는 필수 입니다."),
    INVALID_SIZE_USER_BIRTHDATE("M008", "생일은 8 글자로 입력해주세요."),
    NULL_USER_BIRTHDATE("M009", "생일은 필수 입니다."),
    ALREADY_INUSE_USER_USERNAME("M010", "이미 존재하는 사용자 아이디입니다."),
    NOT_FOUND_USER_USERNAME("M011", "존재하지 않는 사용자 아이디입니다."),
    INVALID_USER_ID_PASSWORD("M012", "아이디 또는 비밀번호가 잘못 입력 되었습니다."),

    /**
     * F: 파일 업로드 관련
     */
    FAILURE_FILE_UPLOAD("F001", "파일 업로드에 실패하였습니다."),
    ;

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
