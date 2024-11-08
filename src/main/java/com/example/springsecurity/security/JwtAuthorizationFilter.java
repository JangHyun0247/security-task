package com.example.springsecurity.security;

import com.example.springsecurity.user.util.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.springsecurity.security.JwtProvider.AUTHORIZATION_HEADER;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException {

        if (req.getRequestURI().equals("/signup") || req.getRequestURI().equals("/sign")) {
            filterChain.doFilter(req, res);
            return;
        }

        String accessToken = jwtProvider.getAccessTokenFromHeader(req);
        String username = jwtProvider.getUsernameFromToken(accessToken);

        if (StringUtils.hasText(accessToken)) {
            if (jwtProvider.validateAccessToken(accessToken)) {
                log.info("액세스 토큰 검증 성공");
                updateToken(accessToken, username, res);
            } else if (jwtProvider.hasRefreshToken(username)) {
                String refreshToken = jwtProvider.getRefreshTokenFromHeader(req);
                updateToken(refreshToken, username, res);
                log.info("토큰 Refresh 성공");
            } else {
                jwtExceptionHandler(res, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }
        } else {
            log.error("토큰 없음");
        }
        filterChain.doFilter(req, res);
    }

    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(createAuthentication(username));
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void jwtExceptionHandler(HttpServletResponse res, HttpStatus status, String msg) {
        res.setStatus(status.value());
        res.setContentType("application/json");
        try {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", status.value());
            responseMap.put("message", msg);
            String json = new ObjectMapper().writeValueAsString(responseMap);
            res.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void updateToken(String token, String username, HttpServletResponse res) throws IOException {
        UserRole role = jwtProvider.getRoleFromToken(token);
        String newAccessToken = jwtProvider.createAccessToken(username, role);

        res.setHeader(AUTHORIZATION_HEADER, newAccessToken);
        setAuthentication(jwtProvider.getUsernameFromToken(newAccessToken));

        res.setContentType("application/json");
        res.getWriter().write("{\"status\": " + HttpStatus.OK.value() + ", \"message\": \"토큰 갱신 성공\", \"newAccessToken\": \"" + newAccessToken + "\"}");
    }
}