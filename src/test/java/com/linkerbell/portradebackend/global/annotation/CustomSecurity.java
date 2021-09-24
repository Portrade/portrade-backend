package com.linkerbell.portradebackend.global.annotation;

import com.linkerbell.portradebackend.domain.user.controller.UserController;
import com.linkerbell.portradebackend.global.config.SecurityConfig;
import com.linkerbell.portradebackend.global.config.security.CustomAccessDeniedHandler;
import com.linkerbell.portradebackend.global.config.security.CustomAuthenticationEntryPoint;
import com.linkerbell.portradebackend.global.config.security.jwt.JwtFilter;
import com.linkerbell.portradebackend.global.config.security.jwt.TokenProvider;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.lang.annotation.*;

@WebMvcTest({UserController.class,
        SecurityConfig.class,
        JwtFilter.class,
        TokenProvider.class,
        CustomAuthenticationEntryPoint.class,
        CustomAccessDeniedHandler.class})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Target(ElementType.TYPE)
public @interface CustomSecurity {
}
