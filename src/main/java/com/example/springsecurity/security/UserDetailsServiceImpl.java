package com.example.springsecurity.security;

import com.example.springsecurity.user.entity.User;
import com.example.springsecurity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws RuntimeException {
        User user = userRepository.findByUsername(username).orElseThrow( () -> new RuntimeException("사용자 찾을 수 없습니다."));
        return new UserDetailsImpl(user);
    }

}
