package com.example.springsecurity.user.dto;

import lombok.Getter;

@Getter
public class SignRequestDto {
    private final String username;
    private final String password;

    public SignRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
