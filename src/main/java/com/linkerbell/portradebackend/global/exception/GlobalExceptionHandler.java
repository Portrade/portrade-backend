package com.linkerbell.portradebackend.global.exception;


import com.linkerbell.portradebackend.global.exception.custom.InvalidTokenException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthenticatedException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UnAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthenticatedException(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .method(request.getMethod())
                .path(request.getRequestURI())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(value = UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthorizedException(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .method(request.getMethod())
                .path(request.getRequestURI())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .method(request.getMethod())
                .path(request.getRequestURI())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .method(request.getMethod())
                .path(request.getRequestURI())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    //validation exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .method(request.getMethod())
                .path(request.getRequestURI())
                .message(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}