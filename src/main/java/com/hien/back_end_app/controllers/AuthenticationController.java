package com.hien.back_end_app.controllers;


import com.cloudinary.Api;
import com.hien.back_end_app.dto.request.*;
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

    @PostMapping("/register")
    public ApiResponse registerStandardAccount(@Valid @RequestBody RegisterRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("register new account successfully")
                .status(200)
                .data(authenticationService.register(dto))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse logout(@Valid @RequestBody LogoutRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("logout successfully")
                .status(200)
                .data(authenticationService.logout(dto))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse refresh(@Valid @RequestBody RefreshRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("refresh token successfully")
                .status(200)
                .data(authenticationService.refresh(dto))
                .build();
    }

    @PatchMapping("/change-password")
    public ApiResponse changePassword(@Valid @RequestBody ChangePasswordRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("change passwords successfully")
                .status(200)
                .data(authenticationService.changePassword(dto))
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("returning reset token successfully")
                .status(200)
                .data(authenticationService.forgot(dto))
                .build();
    }

    @PatchMapping("/reset-password")
    public ApiResponse resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("reset password successfully")
                .status(200)
                .data(authenticationService.reset(dto))
                .build();
    }
}
