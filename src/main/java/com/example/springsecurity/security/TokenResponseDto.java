package com.example.springsecurity.security;

import lombok.Getter;

@Getter
public class TokenResponseDto {
    private final String token;

    public TokenResponseDto(String token) {
        this.token = token;
    }
}
