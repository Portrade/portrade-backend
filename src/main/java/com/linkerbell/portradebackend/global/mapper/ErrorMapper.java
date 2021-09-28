package com.linkerbell.portradebackend.global.mapper;

import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.ErrorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.servlet.http.HttpServletRequest;

@Mapper(componentModel = "spring")
public interface ErrorMapper {

    @Mapping(target = "code", source = "errorCode.code")
    @Mapping(target = "message", source = "errorCode.message")
    @Mapping(target = "path", source = "request.requestURI")
    ErrorResponse toDto(ErrorCode errorCode, HttpServletRequest request);
}
