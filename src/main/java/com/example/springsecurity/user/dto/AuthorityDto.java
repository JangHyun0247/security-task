package com.example.springsecurity.user.dto;

import lombok.Getter;

@Getter
public class AuthorityDto {
    private final String authorityName;

    public AuthorityDto(String authorityName) {
        this.authorityName = authorityName;
    }
}
