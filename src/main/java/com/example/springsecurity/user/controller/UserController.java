package com.example.springsecurity.user.controller;

import com.example.springsecurity.user.dto.SignupRequestDto;
import com.example.springsecurity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("회원가입에 성공하였습니다.");
    }
}
