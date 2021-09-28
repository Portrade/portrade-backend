package com.linkerbell.portradebackend.global.config;


import com.linkerbell.portradebackend.global.config.security.CustomAccessDeniedHandler;
import com.linkerbell.portradebackend.global.config.security.CustomAuthenticationEntryPoint;
import com.linkerbell.portradebackend.global.config.security.jwt.JwtFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtFilter jwtFilter;

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String PREFIX_URL = "/api/v1";

        http.csrf().disable();
        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().disable()
                .logout().disable();

        http.exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers(PREFIX_URL + "/auth/logout").authenticated()

                .antMatchers(HttpMethod.POST, PREFIX_URL + "/notices").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, PREFIX_URL + "/notices/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, PREFIX_URL + "/notices/**").hasRole("ADMIN")

                .antMatchers(PREFIX_URL + "/auth/user").authenticated()
                .antMatchers(PREFIX_URL + "/auth/admin").hasRole("ADMIN")
                .anyRequest().permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
