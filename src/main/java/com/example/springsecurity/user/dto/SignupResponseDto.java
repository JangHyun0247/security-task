package com.example.springsecurity.user.dto;

import com.example.springsecurity.user.entity.User;
import com.example.springsecurity.user.util.UserRole;
import lombok.Getter;

@Getter
public class SignupResponseDto {
    private final String username;
    private final String nickname;
    private final UserRole userRole;

    public SignupResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getUsername();
        this.userRole = user.getUserRole();
    }
}
