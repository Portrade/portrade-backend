package com.linkerbell.portradebackend.global.config;

import com.linkerbell.portradebackend.domain.user.domain.Role;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.UUID;

public class WithMockPortradeAdminSecurityContextFactory implements WithSecurityContextFactory<WithMockPortradeAdmin> {


    @Override
    public SecurityContext createSecurityContext(WithMockPortradeAdmin annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User admin = User.builder()
                .id(UUID.fromString("d9b4adce-82bd-48fe-9456-cfb20d43537d"))
                .username("admin1")
                .password("1234Aa@@")
                .name("김관리")
                .birthDate("19771112")
                .build();

        admin.addRole(Role.ROLE_ADMIN);

        UserAdapter principal = new UserAdapter(admin);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
