package com.example.springsecurity.user.dto;

import com.example.springsecurity.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SignupResponseDto {
    private final String username;
    private final String nickname;
    private final List<AuthorityDto> authorities;

    public SignupResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getUsername();
        this.authorities = List.of(new AuthorityDto(user.getUserRole().getAuthorityName()));
    }
}
