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
import org.springframework.web.cors.CorsUtils;


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
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(PREFIX_URL + "/auth/logout").authenticated()
                .antMatchers(PREFIX_URL + "/users/me/insight").authenticated()
                .antMatchers(PREFIX_URL + "/users/me/profile/**").authenticated()
                .antMatchers(PREFIX_URL + "/users/{userId}/follow").authenticated()

                .antMatchers(HttpMethod.POST, PREFIX_URL + "/notices").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, PREFIX_URL + "/notices/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/notices/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, PREFIX_URL + "/qnas").authenticated()
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/qnas/{qnaId}").authenticated()
                .antMatchers(PREFIX_URL + "/qnas/{qnaId}/answer").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, PREFIX_URL + "/faqs").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/faqs/{faqId}").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, PREFIX_URL + "/companies").authenticated()
                .antMatchers(HttpMethod.PUT, PREFIX_URL + "/companies/{companyId}").authenticated()

                .antMatchers(HttpMethod.POST, PREFIX_URL + "/portfolios").authenticated()
                .antMatchers(HttpMethod.PUT, PREFIX_URL + "/portfolios/{portfolioId}").authenticated()
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/portfolios/{portfolioId}").authenticated()

                .anyRequest().permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
