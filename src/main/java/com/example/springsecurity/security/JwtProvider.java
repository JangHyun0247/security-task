package com.example.springsecurity.security;

import com.example.springsecurity.refreshtoken.repository.RefreshTokenRepository;
import com.example.springsecurity.refreshtoken.service.RefreshTokenService;
import com.example.springsecurity.user.util.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";

    public static final Long ACCESS_TOKEN_TIME = 1000L * 60 * 60; // 1시간
    public static final Long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 2주

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt-secret-key}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(String username, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(String username, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 요청 바디에서 액세스 토큰 추출
     */
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return substringToken(bearerToken);
        } else {
            return bearerToken;
        }
    }

    /**
     * 요청 헤더에서 리프레시 토큰 추출
     */
    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token"); // 예시로 "Refresh-Token" 헤더에서 리프레시 토큰을 추출

        if (StringUtils.hasText(refreshToken)) {
            return refreshToken;
        }
        return null;  // 리프레시 토큰이 없으면 null 반환
    }

    /**
     * Access 토큰 검증
     */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    /**
     * Refresh 토큰 검증
     */
    public boolean hasRefreshToken(String username) {
        return refreshTokenRepository.findByUsername(username).isPresent();
    }

    /**
     * 토큰에서 username 가져오기
     */
    public String getUsernameFromToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            token = substringToken(token);
        }
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우에도 가져옴
            return e.getClaims().getSubject();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 토큰에서 role 가져오기
     */
    public UserRole getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody();
        String role = claims.get(AUTHORIZATION_KEY, String.class);
        return UserRole.valueOf(role);
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length());
        } else if (StringUtils.hasText(tokenValue)) {
            return tokenValue;
        }
        throw new NullPointerException("토큰이 없습니다.");
    }
}