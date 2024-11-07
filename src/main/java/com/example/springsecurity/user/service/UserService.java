package com.example.springsecurity.user.service;

import com.example.springsecurity.user.dto.SignupRequestDto;
import com.example.springsecurity.user.dto.SignupResponseDto;
import com.example.springsecurity.user.repository.UserRepository;
import com.example.springsecurity.user.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signup(SignupRequestDto requestDto) {
        UserRole role = UserRole.ROLE_USER;
        if (requestDto.g)
    }
}
