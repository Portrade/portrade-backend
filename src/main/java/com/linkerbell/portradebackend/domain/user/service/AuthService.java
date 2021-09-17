package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.dto.LogInRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.TokenResponseDto;
import com.linkerbell.portradebackend.global.config.security.jwt.TokenProvider;
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
        try {
            authenticationToken = new UsernamePasswordAuthenticationToken(logInRequestDto.getId(), logInRequestDto.getPassword());
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못 입력 되었습니다.");
        }

        String accessToken = tokenProvider.createAccessToken(authentication);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
