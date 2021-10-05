package com.linkerbell.portradebackend.global.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockPortradeUserSecurityContextFactory.class)
public @interface WithMockPortradeUser {
}