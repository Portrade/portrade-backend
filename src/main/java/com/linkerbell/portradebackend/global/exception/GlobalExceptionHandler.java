package com.linkerbell.portradebackend.global.exception;


import com.linkerbell.portradebackend.global.exception.custom.InvalidTokenException;
import com.linkerbell.portradebackend.global.exception.custom.InvalidValueException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthenticatedException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import com.linkerbell.portradebackend.global.mapper.ErrorMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorMapper errorMapper = Mappers.getMapper(ErrorMapper.class);

    @ExceptionHandler(value = UnAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthenticatedException(UnAuthenticatedException e, HttpServletRequest request) {
        ErrorResponse errorResponse = errorMapper.toDto(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(value = UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnAuthorizedException e, HttpServletRequest request) {
        ErrorResponse errorResponse = errorMapper.toDto(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException e, HttpServletRequest request) {
        ErrorResponse errorResponse = errorMapper.toDto(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.valueOf(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        ErrorResponse errorResponse = errorMapper.toDto(errorCode, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = InvalidValueException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(InvalidValueException e, HttpServletRequest request) {
        ErrorResponse errorResponse = errorMapper.toDto(e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}