package com.example.springsecurity.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    private final String username;
    private final String password;
    private final String nickname;

    @JsonCreator
    public SignupRequestDto(@JsonProperty("username")String username,
                            @JsonProperty("password") String password,
                            @JsonProperty("nickname") String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}
