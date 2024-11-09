package com.example.springsecurity.user.refreshtoken.service;

import com.example.springsecurity.user.refreshtoken.entity.RefreshToken;
import com.example.springsecurity.user.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(String username, String refreshToken) {
        RefreshToken token = new RefreshToken(username, refreshToken);
        refreshTokenRepository.save(token);
    }
}
