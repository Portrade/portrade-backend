package com.linkerbell.portradebackend.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /**
     * C: 공통
     */
    NONEXISTENT_AUTHENTICATION("C001", "로그인이 필요합니다."),
    NONEXISTENT_AUTHORIZATION("C002", "권한이 없습니다."),

    /**
     * M: User 관련
     */
    NONEXISTENT_USER("M001", "존재하지 않는 사용자입니다."),
    // 유효성
    INVALID_USER_ID("M100", "아이디는 영문 소문자, 숫자만 입력하세요."),
    NULL_USER_ID("M101", "아이디는 필수입니다."),
    INVALID_USER_NAME("M102", "이름을 2~8자 사이로 입력해주세요."),
    NULL_USER_NAME("M103", "이름은 필수입니다."),
    INVALID_USER_PASSWORD("M104", "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함 8글자 이상이어야 합니다."),
    NULL_USER_PASSWORD("M105", "비밀번호는 필수입니다."),
    NULL_USER_WANTEDJOB("M106", "희망직무는 필수입니다."),
    INVALID_SIZE_USER_BIRTHDATE("M107", "생일은 8글자로 입력해주세요."),
    NULL_USER_BIRTHDATE("M108", "생일은 필수입니다."),
    DUPLICATED_USER_USERNAME("M109", "이미 존재하는 사용자 아이디입니다."),
    NULL_USER_JOB("M110", "구직상태는 필수입니다."),
    // 로그인
    INVALID_USER_ID_PASSWORD("M200", "아이디 또는 비밀번호가 잘못 입력되었습니다."),

    /**
     * N: Notice 관련
     */
    NONEXISTENT_NOTICE("N001", "존재하지 않는 공지사항입니다."),
    // 유효성
    NULL_NOTICE_TITLE("N100", "제목은 필수입니다."),
    NULL_NOTICE_CONTENT("N101", "내용은 필수입니다."),

    /**
     * Q: Qna 관련
     */
    NONEXISTENT_QNA("Q001", "존재하지 않는 문의글입니다."),
    // 유효성
    NULL_QNA_CATEGORY("Q100", "카테고리는 필수입니다."),
    NULL_QNA_PHONENUMBER("Q101", "휴대전화 번호는 필수입니다."),
    INVALID_QNA_PHONENUMBER("Q102", "휴대전화 번호는 8글자로 입력해주세요."),
    NULL_QNA_EMAIL("Q103", "이메일은 필수입니다."),
    INVALID_QNA_EMAIL("Q104", "올바르지 않은 이메일 형식입니다."),
    NULL_QNA_TITLE("Q105", "제목은 필수입니다."),
    INVALID_QNA_TITLE("Q106", "제목은 1~20자 사이로 입력해주세요."),
    NULL_QNA_CONTENT("Q107", "문의글 내용은 필수입니다."),
    INVALID_QNA_CONTENT("Q108", "문의글 내용은 1~500자 사이로 입력해주세요."),
    NONEXISTENT_QNA_ID("Q109", "존재하지 않는 문의글입니다."),
    NULL_QNA_ISPUBLIC("Q110", "공개/비공개 여부를 선택해주세요."),

    /**
     * F: Faq 관련
     */
    NONEXISTENT_FAQ("F001", "존재하지 않는 자주묻는질문 글입니다."),
    //유효성
    NULL_FAQ_TITLE("F101", "제목은 필수입니다."),
    INVALID_FAQ_TITLE("F102", "제목은 1~20자 사이로 입력해주세요."),
    NULL_FAQ_CONTENT("F103", "내용은 필수입니다."),
    INVALID_FAQ_CONTENT("F104", "내용은 1~500자 사이로 입력해주세요."),

    /**
     * D: Company 관련
     */
    NONEXISTENT_COMPANY("D001", "존재하지 않는 기업입니다."),
    //유효성
    DUPLICATED_COMPANY("D100", "이미 존재하는 기업입니다."),
    NULL_COMPANY_NAME("D101", "기업명은 필수입니다."),
    NULL_COMPANY_FORM("D102", "기업형태는 필수입니다."),
    NULL_COMPANY_INDUSTRY("D103", "업종은 필수입니다."),
    NULL_COMPANY_SALES("D104", "매출액은 필수입니다."),
    NULL_COMPANY_HOMEPAGE("D105", "홈페이지는 필수입니다."),
    NULL_COMPANY_ADDRESS("D106", "기업주소는 필수입니다."),
    NULL_COMPANY_CEO("D107", "대표자명은 필수입니다."),

    /**
     * R: Recruitment 관련
     */
    NONEXISTENT_RECRUITMENT("R001", "존재하지 않는 기업 공고입니다."),
    //유효성
    NULL_RECRUITMENT_TITLE("R100", "공고명은 필수입니다."),
    NULL_RECRUITMENT_CAREER("R101", "경력은 필수입니다."),
    NULL_RECRUITMENT_EDUCATION("R102", "학력은 필수입니다."),
    NULL_RECRUITMENT_WORKTYPE("R103", "업종은 필수입니다."),
    NULL_RECRUITMENT_PAY("R104", "매출액은 필수입니다."),
    NULL_RECRUITMENT_ADDRESS("R105", "장소는 필수입니다."),
    NULL_RECRUITMENT_CATEGORY("R106", "카테고리는 필수입니다."),

    /**
     * P: Portfolio 관련
     */
    NONEXISTENT_PORTFOLIO("P001", "존재하지 않는 포트폴리오입니다."),
    // 유효성
    NULL_PORTFOLIO_TITLE("P100", "제목은 필수입니다."),
    NULL_PORTFOLIO_DESCRIPTION("P101", "설명은 필수입니다."),
    INVALID_PORTFOLIO_DESCRIPTION("P102", "설명은 1~500자 사이로 입력해주세요."),
    NULL_PORTFOLIO_CATEGORY("P103", "카테고리는 필수입니다."),
    NULL_PORTFOLIO_ISPUBLIC("P104", "공개/비공개 선택은 필수입니다."),
    NULL_PORTFOLIO_MAINIMAGE("P105", "대표 이미지는 필수입니다."),
    NULL_PORTFOLIO_CONTENTFILES("P106", "포트폴리오 파일은 필수입니다."),

    /**
     * E: Comment 관련
     */
    NONEXISTENT_COMMENT("E001", "존재하지 않는 댓글입니다."),
    // 유효성
    NULL_COMMENT_CONTENT("E100", "내용은 필수입니다."),

    /**
     * G: 파일 업로드 관련
     */
    NONEXISTENT_FILE("G001", "존재하지 않는 파일입니다."),
    // 업로드
    FILE_UPLOAD_FAILURE("G100", "파일 업로드에 실패하였습니다."),
    INVALID_FILE_NAME("G101", "올바르지 않은 파일명입니다."),
    INVALID_FILE_EXTENSION("G102", "올바르지 않은 파일 확장자입니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
