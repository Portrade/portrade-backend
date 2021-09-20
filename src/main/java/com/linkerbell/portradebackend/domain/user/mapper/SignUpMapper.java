package com.linkerbell.portradebackend.domain.user.mapper;

import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.global.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SignUpMapper extends GenericMapper<SignUpRequestDto, User> {

    @Mapping(target = "profile.college", source = "dto.college")
    @Mapping(target = "profile.isGraduated", source = "dto.getGraduated")
    @Mapping(target = "password", ignore = true)
    User toEntity(SignUpRequestDto dto, Profile profile);
}
