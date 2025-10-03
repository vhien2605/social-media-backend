package com.hien.back_end_app.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hien.back_end_app.dto.request.LoginStandardRequestDTO;
import com.hien.back_end_app.dto.request.RefreshRequestDTO;
import com.hien.back_end_app.dto.request.RegisterRequestDTO;
import com.hien.back_end_app.dto.response.auth.JwtResponseDTO;
import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.services.AuthenticationService;
import com.hien.back_end_app.services.JwtService;
import com.hien.back_end_app.utils.enums.ErrorCode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthenticationService authenticationService;
    @MockitoBean
    private JwtService jwtService;

    @Test
    public void loginTest() throws Exception {
        Mockito.when(authenticationService.login(Mockito.any(LoginStandardRequestDTO.class)))
                .thenReturn(JwtResponseDTO.builder()
                        .accessToken("access")
                        .refreshToken("refresh")
                        .build());
        ObjectMapper objectMapper = new ObjectMapper();
        LoginStandardRequestDTO dto = LoginStandardRequestDTO.builder()
                .email("email@email.com")
                .password("password")
                .build();
        String bodyEncoded = objectMapper.writeValueAsString(dto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(bodyEncoded)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("login successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.accessToken").value("access"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.refreshToken").value("refresh"));
    }

    @Test
    public void loginFailureTest() throws Exception {
        Mockito.when(authenticationService.login(Mockito.any(LoginStandardRequestDTO.class)))
                .thenThrow(new AppException(ErrorCode.UNAUTHORIZED));
        ObjectMapper objectMapper = new ObjectMapper();
        LoginStandardRequestDTO dto = LoginStandardRequestDTO.builder()
                .email("email@email.com")
                .password("password")
                .build();
        String bodyEncoded = objectMapper.writeValueAsString(dto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(bodyEncoded)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("user unauthorized"));
    }

    @Test
    public void registerTest() throws Exception {
        Mockito.when(authenticationService.register(Mockito.any(RegisterRequestDTO.class)))
                .thenReturn(UserResponseDTO.builder()
                        .fullName("vu hien")
                        .email("hvuasdasd@gmail.com")
                        .education("life")
                        .build());

        RegisterRequestDTO dto = RegisterRequestDTO.builder()
                .fullName("adasd")
                .email("hadasd@gmail.com")
                .password("asasdsd")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyEncoded = objectMapper.writeValueAsString(dto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(bodyEncoded)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("register new account successfully"));
    }

    @Test
    public void refreshTokenTest() throws Exception {
        Mockito.when(authenticationService.refresh(Mockito.any(RefreshRequestDTO.class)))
                .thenReturn(JwtResponseDTO.builder()
                        .refreshToken("refresh")
                        .accessToken("access")
                        .build());
        RefreshRequestDTO dto = RefreshRequestDTO.builder()
                .refreshToken("asdsaasd")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyEncoded = objectMapper.writeValueAsString(dto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(bodyEncoded)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("refresh token successfully"));

    }
}
