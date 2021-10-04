package com.linkerbell.portradebackend.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /**
     * C: 공통
     */

    /**
     * M: User 관련
     */
    // 회원가입
    INVALID_USER_ID("M001", "아이디는 영문 소문자, 숫자만 입력하세요."),
    NULL_USER_ID("M002", "아이디는 필수입니다."),
    INVALID_USER_NAME("M003", "이름을 2~8자 사이로 입력해주세요."),
    NULL_USER_NAME("M004", "이름은 필수입니다."),
    INVALID_USER_PASSWORD("M005", "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함 8글자 이상이어야 합니다."),
    NULL_USER_PASSWORD("M006", "비밀번호는 필수입니다."),
    NULL_USER_WANTEDJOB("M007", "희망직무는 필수입니다."),
    INVALID_SIZE_USER_BIRTHDATE("M008", "생일은 8글자로 입력해주세요."),
    NULL_USER_BIRTHDATE("M009", "생일은 필수입니다."),
    DUPLICATED_USER_USERNAME("M010", "이미 존재하는 사용자 아이디입니다."),
    NOT_FOUND_USER_USERNAME("M011", "존재하지 않는 사용자 아이디입니다."),
    // 로그인
    INVALID_USER_ID_PASSWORD("M012", "아이디 또는 비밀번호가 잘못 입력되었습니다."),
    // 인증
    NONEXISTENT_AUTHORITY("M201", "권한이 없습니다."),
    NONEXISTENT_USER("M202", "존재하지 않는 사용자입니다."),

    /**
     * N: Notice 관련
     */
    NONEXISTENT_NOTICE_ID("N001", "존재하지 않는 공지사항 번호입니다."),
    // 작성, 수정
    NULL_NOTICE_TITLE("N100", "제목은 필수입니다."),
    NULL_NOTICE_CONTENT("N101", "내용은 필수입니다."),

    /**
     * Q: Qna 관련
     */
    NULL_QNA_CATEGORY("Q001", "카테고리는 필수입니다."),
    NULL_QNA_PHONENUMBER("Q002", "휴대전화 번호는 필수입니다."),
    INVALID_QNA_PHONENUMBER("Q003", "휴대전화 번호는 8글자로 입력해주세요."),
    NULL_QNA_EMAIL("Q004", "이메일은 필수 입니다."),
    INVALID_QNA_EMAIL("Q005", "올바르지 않은 이메일 형식입니다."),
    NULL_QNA_TITLE("Q006", "제목은 필수 입니다."),
    INVALID_QNA_TITLE("Q007", "제목은 1~20자 사이로 입력해주세요."),
    NULL_QNA_CONTENT("Q008", "문의글 내용은 필수입니다."),
    INVALID_QNA_CONTENT("Q009", "문의글 내용은 1~500자 사이로 입력해주세요."),
    NONEXISTENT_QNA_ID("Q010", "존재하지 않는 문의글 입니다."),
    NULL_QNA_ISPUBLIC("Q011", "공개/비공개 여부를 선택해주세요."),

    NONEXISTENT_QNA("Q201", "존재하지 않는 문의글입니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
