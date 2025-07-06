package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.LoginStandardRequestDTO;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Valid LoginStandardRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("login successfully")
                .status(200)
                .data(authenticationService.login(dto))
                .build();
    }
}
