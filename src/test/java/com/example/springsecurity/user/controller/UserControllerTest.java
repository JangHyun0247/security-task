package com.example.springsecurity.user.controller;

import com.example.springsecurity.user.dto.SignupRequestDto;
import com.example.springsecurity.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 전체 스프링 컨텍스트 로딩
@AutoConfigureMockMvc // MockMvc 설정 자동화
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // HTTP 요청을 모킹하는 MockMvc

    @MockBean // UserService를 모킹
    private UserService userService;

    private SignupRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new SignupRequestDto("testuser", "testnickname", "testpassword");
    }

    @Test
    void testSignup() throws Exception {
        // 서비스 메소드 호출 시 mock 응답 설정
        doNothing().when(userService).signup(requestDto);

        // POST 요청을 보낸 후 응답 결과를 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"nickname\":\"testnickname\",\"password\":\"testpassword\"}")
                        .with(csrf()))  // CSRF 토큰 포함
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testuser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("testnickname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorities[0].authority").value("ROLE_USER"));

        // signup 메소드가 한 번 호출됐는지 검증
        verify(userService).signup(requestDto);
    }
}
