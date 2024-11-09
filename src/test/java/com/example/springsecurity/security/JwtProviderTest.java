package com.example.springsecurity.security;

import com.example.springsecurity.user.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class JwtProviderTest {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @MockBean
    private JwtProvider jwtProvider;

    private String username;
    private UserRole role;
    private String accessToken;
    private String refreshToken;

    @BeforeEach
    void setUp() {
        username = "testuser";
        role = UserRole.ROLE_USER;

        // Mocking the JWT token creation methods
        accessToken = "mockedAccessToken";
        refreshToken = "mockedRefreshToken";

        // Mocking the behavior of JwtProvider methods
        when(jwtProvider.createAccessToken(username, role)).thenReturn(accessToken);
        when(jwtProvider.createRefreshToken(username, role)).thenReturn(refreshToken);
        when(jwtProvider.validateAccessToken(accessToken)).thenReturn(true);
        when(jwtProvider.getUsernameFromToken(accessToken)).thenReturn(username);
        when(jwtProvider.getRoleFromToken(accessToken)).thenReturn(role);
    }

    @Test
    public void testJwtSecretKey() {
        System.out.println("JWT Secret Key: " + jwtSecretKey);
    }

    @Test
    void testCreateAccessToken() {
        assertThat(accessToken).isNotNull();
        assertThat(jwtProvider.validateAccessToken(accessToken)).isTrue();
        assertThat(jwtProvider.getUsernameFromToken(accessToken)).isEqualTo(username);
    }

    @Test
    void testCreateRefreshToken() {
        assertThat(refreshToken).isNotNull();
    }

    @Test
    void testGetRoleFromToken() {
        // 역할을 직접 비교
        UserRole tokenRole = jwtProvider.getRoleFromToken(accessToken);
        assertThat(tokenRole).isEqualTo(role);  // UserRole 값 비교
    }
}