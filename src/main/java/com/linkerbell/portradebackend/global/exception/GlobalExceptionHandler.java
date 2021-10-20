package com.linkerbell.portradebackend.global.exception;


import com.linkerbell.portradebackend.global.exception.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UnAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthenticatedException(UnAuthenticatedException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(value = UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnAuthorizedException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.valueOf(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = InvalidValueException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(InvalidValueException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = NotExistException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(NotExistException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(value = NotUniqueException.class)
    public ResponseEntity<ErrorResponse> handleNotUniqueException(NotUniqueException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}