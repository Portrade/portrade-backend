package com.linkerbell.portradebackend.domain.qna.mapper;

import com.linkerbell.portradebackend.domain.qna.dto.CreateQnaRequestDto;
import com.linkerbell.portradebackend.domain.qna.dto.QnaResponseDto;
import com.linkerbell.portradebackend.domain.qna.dto.ReplyQnaRequestDto;
import com.linkerbell.portradebackend.domain.qna.domain.Answer;
import com.linkerbell.portradebackend.domain.qna.domain.Qna;
import com.linkerbell.portradebackend.domain.qna.domain.Question;
import com.linkerbell.portradebackend.domain.qna.domain.Status;
import com.linkerbell.portradebackend.domain.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QnaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "title", source = "requestDto.title")
    @Mapping(target = "content", source = "requestDto.content")
    @Mapping(target = "isPublic", source = "requestDto.secret")
    Answer toEntity(ReplyQnaRequestDto requestDto, User user, Question question);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "requestDto.name")
    @Mapping(target = "isPublic", source = "requestDto.secret")
    Question toEntity(CreateQnaRequestDto requestDto, User user, Status status);

    QnaResponseDto toDto(Qna qna);


}
