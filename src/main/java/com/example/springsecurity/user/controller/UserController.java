package com.example.springsecurity.user.controller;

import com.example.springsecurity.user.dto.AuthorityDto;
import com.example.springsecurity.user.dto.SignupRequestDto;
import com.example.springsecurity.user.dto.SignupResponseDto;
import com.example.springsecurity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return new SignupResponseDto(
                requestDto.getUsername(),
                requestDto.getNickname(),
                List.of(new AuthorityDto("ROLE_USER"))
        );
    }
}
