package com.linkerbell.portradebackend.global.config;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.UUID;

public class WithMockPortradeUserSecurityContextFactory implements WithSecurityContextFactory<WithMockPortradeUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockPortradeUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserAdapter principal =
                new UserAdapter(User.builder()
                        .id(UUID.fromString("3cbe539a-33ba-4550-a82c-63be333ac2d0"))
                        .username("user1")
                        .password("1234Aa@@")
                        .name("유저1")
                        .birthDate("19900903")
                        .wantedJob("designer")
                        .build());

        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
