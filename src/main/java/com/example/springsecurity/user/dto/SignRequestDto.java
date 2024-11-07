package com.example.springsecurity.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class SignRequestDto {
    private final String username;
    private final String password;

    @JsonCreator
    public SignRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
