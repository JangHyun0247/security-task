package com.example.springsecurity.user.util;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_MANAGER("MANAGER_USER");

    private final String authorityName;

    UserRole(String authorityName) {
        this.authorityName = authorityName;
    }
}
