package com.linkerbell.portradebackend.global.exception;

import lombok.*;

import javax.servlet.http.HttpServletRequest;
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
    public ErrorResponse(String method, String path, String message, String code) {
        this.method = method;
        this.path = path;
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(ErrorCode e, HttpServletRequest request) {
        return ErrorResponse.builder()
                .method(request.getMethod())
                .path(request.getRequestURI())
                .message(e.getMessage())
                .code(e.getCode())
                .build();
    }
}