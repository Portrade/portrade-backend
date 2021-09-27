package com.linkerbell.portradebackend.global.exception;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String method;
    private String path;
    private LocalDateTime timestamp;
    private String message;
    private String code;

    @Builder
    public ErrorResponse(String method, String path, LocalDateTime timestamp, String message, String code) {
        this.method = method;
        this.path = path;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.code = code;
    }
}