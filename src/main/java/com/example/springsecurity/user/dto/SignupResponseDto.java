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
}
