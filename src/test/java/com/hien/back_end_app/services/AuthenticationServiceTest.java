package com.hien.back_end_app.services;

import com.hien.back_end_app.dto.request.LoginStandardRequestDTO;
import com.hien.back_end_app.dto.request.RegisterRequestDTO;
import com.hien.back_end_app.dto.response.auth.JwtResponseDTO;
import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void loginServiceTest() {
        LoginStandardRequestDTO dto = LoginStandardRequestDTO.builder()
                .email("hvu6582@gmail.com")
                .password("hienhien123@")
                .build();
        JwtResponseDTO result = authenticationService.login(dto);
        Assertions.assertNotEquals(0, result.getAccessToken().length());
        Assertions.assertNotEquals(0, result.getRefreshToken().length());
    }

    @Test
    public void registerTest() {
        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .fullName("assad")
                .email("2300@gmail.com")
                .password("hienhien123@")
                .build();
        UserResponseDTO result = authenticationService.register(dto);
        Assertions.assertEquals(dto.getEmail(), result.getEmail());
    }
}
