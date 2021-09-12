package com.linkerbell.portradebackend.global.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String method;
    private String path;
    private LocalDateTime timestamp;
    private String message;

    @Builder
    public ErrorResponse(String method, String path, LocalDateTime timestamp, String message) {
        this.method = method;
        this.path = path;
        this.timestamp = timestamp;
        this.message = message;
    }
}