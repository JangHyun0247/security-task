package com.example.springsecurity.user.service;

import com.example.springsecurity.user.dto.SignupRequestDto;
import com.example.springsecurity.user.entity.User;
import com.example.springsecurity.user.repository.UserRepository;
import com.example.springsecurity.user.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private SignupRequestDto signupRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mock 초기화
        signupRequestDto = new SignupRequestDto("testUser", "testPassword", "testNickname");
    }

    @Test
    void testSignup_UserAlreadyExists() {
        // given
        when(userRepository.existsByUsername(any())).thenReturn(true);  // 이미 사용자가 존재한다고 가정

        // when, then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.signup(signupRequestDto);  // 가입 시도
        });

        assertEquals("이미 등록된 사용자입니다.", exception.getMessage());  // 예외 메시지 확인
    }

    @Test
    void testSignup_Success() {
        // given
        when(userRepository.existsByUsername(any())).thenReturn(false);  // 사용자 존재하지 않음
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");  // 패스워드 인코딩

        // when
        userService.signup(signupRequestDto);  // 가입 시도

        // then
        verify(userRepository, times(1)).save(any(User.class));  // save 메서드가 한 번 호출되는지 확인
    }
}