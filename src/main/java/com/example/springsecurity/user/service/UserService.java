package com.example.springsecurity.user.service;

import com.example.springsecurity.user.dto.SignupRequestDto;
import com.example.springsecurity.user.entity.User;
import com.example.springsecurity.user.repository.UserRepository;
import com.example.springsecurity.user.util.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        if (existsByUsername(requestDto.getUsername())) {
            throw new RuntimeException("이미 등록된 사용자입니다.");
        }

        UserRole role = UserRole.ROLE_USER;

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(),
                             encodedPassword,
                             requestDto.getNickname(), role);
        userRepository.save(user);
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
