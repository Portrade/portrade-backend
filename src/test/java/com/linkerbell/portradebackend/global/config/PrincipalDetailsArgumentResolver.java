package com.linkerbell.portradebackend.global.config;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@RequiredArgsConstructor
public class PrincipalDetailsArgumentResolver implements HandlerMethodArgumentResolver {

    private final User user1;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        if(Objects.isNull(user1)) {
            Authentication auth =
                    new UsernamePasswordAuthenticationToken("anonymous", null, null);
            return null;
        }

        UserAdapter userDetails = new UserAdapter(user1);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities());
        context.setAuthentication(auth);
        return user1;
    }
}


