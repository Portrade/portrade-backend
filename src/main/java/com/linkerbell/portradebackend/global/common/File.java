package com.linkerbell.portradebackend.global.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;


@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    private String url;
    private String fileName;
    private String extension;

    @Builder
    public File(String url, String fileName, String extension) {
        this.url = url;
        this.fileName = fileName;
        this.extension = extension;
    }

    public void update(String url, String fileName, String extension) {
        this.url = url;
        this.fileName = fileName;
        this.extension = extension;
    }
}

// "CREATE TABLE PORTFOLIO ( PORTFOLIO_ID BIGINT AUTO_INCREMENT PRIMARY KEY, USER_ID BINARY(16), " +
//         "TITLE VARCHAR(255) NOT NULL, DESCRIPTION VARCHAR(500) NOT NULL, CATEGORY VARCHAR(255) NOT NULL, " +
//         "IS_PUBLIC BOOLEAN, VIEW_COUNT INTEGER, EXTENSION VARCHAR(255), FILE_NAME VARCHAR(255), URL VARCHAR(255), " +
//         "CREATED_DATE TIMESTAMP, LAST_MODIFIED_DATE TIMESTAMP, )[*]"; expected "identifier"; SQL statement:
//         create table portfolio ( portfolio_id bigint AUTO_INCREMENT PRIMARY KEY, user_id BINARY(16), title varchar(255) not null, description varchar(500) not null, category varchar(255) not null, is_public boolean, view_count integer, extension varchar(255), file_name varchar(255), url varchar(255), created_date timestamp, last_modified_date timestamp, ) [42001-200]
