package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.LogInRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.TokenResponseDto;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import com.linkerbell.portradebackend.global.config.security.jwt.TokenProvider;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public TokenResponseDto logIn(LogInRequestDto logInRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = null;
        Authentication authentication = null;
        User user = null;
        try {
            user = User.builder()
                    .username(logInRequestDto.getUserId())
                    .password(logInRequestDto.getPassword())
                    .build();

            authenticationToken = new UsernamePasswordAuthenticationToken(new UserAdapter(user), logInRequestDto.getPassword());
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new InvalidValueException(ErrorCode.INVALID_USER_ID_PASSWORD);
        }

        String accessToken = tokenProvider.createAccessToken(authentication);

        return new TokenResponseDto(accessToken);
    }
}
