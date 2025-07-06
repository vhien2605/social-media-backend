package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    @GetMapping("/test")
    public ApiResponse test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ApiSuccessResponse.builder()
                .message("login successfully")
                .status(200)
                .data("vai cut")
                .build();
    }
}
