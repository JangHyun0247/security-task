package com.example.springsecurity.security;

import com.example.springsecurity.user.refreshtoken.service.RefreshTokenService;
import com.example.springsecurity.user.dto.SignupRequestDto;
import com.example.springsecurity.user.util.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.example.springsecurity.security.JwtProvider.AUTHORIZATION_HEADER;
import static com.example.springsecurity.security.JwtProvider.BEARER_PREFIX;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, RefreshTokenService refreshTokenService) {
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        setFilterProcessesUrl("/sign");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            return handleStandardLogin(req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Authentication handleStandardLogin(HttpServletRequest req) throws IOException {
        SignupRequestDto requestDto = new ObjectMapper().readValue(req.getInputStream(), SignupRequestDto.class);
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword(),
                        null
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication authResult) throws IOException {
        log.info("인증 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRole role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getUserRole();

        String accessToken = jwtProvider.createAccessToken(username, role).substring(BEARER_PREFIX.length());
        String refreshToken = jwtProvider.createRefreshToken(username, role);

        res.setHeader(AUTHORIZATION_HEADER,accessToken);

        refreshTokenService.save(username, refreshToken);

        // JSON 응답에 토큰 포함
        res.setStatus(HttpServletResponse.SC_OK);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");

        String jsonResponse = new ObjectMapper().writeValueAsString(new TokenResponseDto(accessToken));
        res.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.getWriter().print("{\"error\":\"Unauthorized\", \"message\":\"" + failed.getMessage() + "\"}");
    }
}
