package com.linkerbell.portradebackend.global.config;


import com.linkerbell.portradebackend.global.config.security.CustomAccessDeniedHandler;
import com.linkerbell.portradebackend.global.config.security.CustomAuthenticationEntryPoint;
import com.linkerbell.portradebackend.global.config.security.jwt.JwtSecurityConfig;
import com.linkerbell.portradebackend.global.config.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring()
                .antMatchers("/h2-console/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/v3/api-docs/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String PREFIX_URL = "/api/v1";

        http.cors();
        http.csrf().disable();
        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().disable()
                .logout().disable();

        http.exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);

        http.apply(new JwtSecurityConfig(tokenProvider));

        http.authorizeRequests()
                // 사용자
                .antMatchers(PREFIX_URL + "/auth/logout").authenticated()
                .antMatchers(PREFIX_URL + "/users/me/insight").authenticated()
                .antMatchers(PREFIX_URL + "/users/me/profile/**").authenticated()
                .antMatchers(PREFIX_URL + "/users/{userId}/follow").authenticated()
                // 공지사항
                .antMatchers(HttpMethod.POST, PREFIX_URL + "/notices").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, PREFIX_URL + "/notices/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/notices/**").hasRole("ADMIN")
                // 1대1 문의
                .antMatchers(HttpMethod.POST, PREFIX_URL + "/qnas").authenticated()
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/qnas/{qnaId}").authenticated()
                .antMatchers(PREFIX_URL + "/qnas/{qnaId}/answer").hasRole("ADMIN")
                // 자주 묻는 질문
                .antMatchers(HttpMethod.POST, PREFIX_URL + "/faqs").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/faqs/{faqId}").hasRole("ADMIN")
                // 포트폴리오
                .antMatchers(HttpMethod.POST, PREFIX_URL + "/portfolios").authenticated()
                .antMatchers(HttpMethod.PUT, PREFIX_URL + "/portfolios/{portfolioId}").authenticated()
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/portfolios/{portfolioId}").authenticated()
                // 포트폴리오 좋아요
                .antMatchers(HttpMethod.PATCH, PREFIX_URL + "/portfolios/{portfolioId}/like").authenticated()
                // 포트폴리오 저장
                .antMatchers(HttpMethod.GET, PREFIX_URL + "/portfolios/save").authenticated()
                .antMatchers(HttpMethod.PATCH, PREFIX_URL + "/portfolios/{portfolioId}/save").authenticated()
                // 기업
                .antMatchers(HttpMethod.POST, PREFIX_URL + "/companies").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, PREFIX_URL + "/companies/{companyId}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/companies/{companyId}").hasRole("ADMIN")
                // 기업 공고
                .antMatchers(HttpMethod.POST, PREFIX_URL + "/recruitments/{companyId}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, PREFIX_URL + "/recruitments/{recruitmentId}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/recruitments/{recruitmentId}").hasRole("ADMIN")

                .anyRequest().permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
