package com.example.springsecurity.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private final String username;
    private final String password;
    private final String nickname;

    public SignupRequestDto(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}
