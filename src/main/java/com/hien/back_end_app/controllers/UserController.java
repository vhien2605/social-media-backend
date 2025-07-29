package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.UserCreationRequestDTO;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/test")
    public ApiResponse test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ApiSuccessResponse.builder()
                .message("login successfully")
                .status(200)
                .data("dep trai qua di ak")
                .build();
    }


    @PostMapping("/create")
    public ApiResponse createUser(@RequestBody UserCreationRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("create user successfully")
                .status(200)
                .data(null)
                .build();
    }
}
