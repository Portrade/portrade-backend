package com.linkerbell.portradebackend.domain.qna.mapper;

import com.linkerbell.portradebackend.domain.qna.dto.QnaCurDetailResponseDto;
import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QnaDetailMapper {
    QnaCurDetailResponseDto toDto(Qna qna);
}
