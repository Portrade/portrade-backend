package com.linkerbell.portradebackend.global.config.security.jwt;

import com.linkerbell.portradebackend.domain.user.service.UserService;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final String SECRET_KEY;
    private final String AUTHORITIES_KEY;
    private final int ACCESS_TOKEN_EXPIRES_IN;
    private final UserService userService;

    private Key decodedSecretKey;

    public TokenProvider(@Value("${jwt.secret-key}") String decodedSecretKey,
                         @Value("${jwt.authorities-key}") String authoritiesKey,
                         @Value("${jwt.access-token-expires-in}") int accessTokenExpiresIn,
                         @Lazy @Autowired UserService userService) {
        this.SECRET_KEY = decodedSecretKey;
        this.AUTHORITIES_KEY = authoritiesKey;
        this.ACCESS_TOKEN_EXPIRES_IN = accessTokenExpiresIn;
        this.userService = userService;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.decodedSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        UserAdapter userAdapter = (UserAdapter) userService.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userAdapter, token, authorities);
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(decodedSecretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token.");
        } catch (Exception e) {
            log.debug("Invalid JWT token.");
        }
        return false;
    }

    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()
                        + ACCESS_TOKEN_EXPIRES_IN * 1000L))
                .signWith(decodedSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
