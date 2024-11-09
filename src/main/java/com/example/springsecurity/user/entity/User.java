package com.example.springsecurity.user.entity;

import com.example.springsecurity.user.util.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String username,  String password, String nickname, UserRole userRole) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }


}
