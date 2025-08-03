package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.FollowService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/follow")
@Validated
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @DeleteMapping("/unfollow/{userId}")
    public ApiResponse unfollow(@PathVariable @Min(value = 0, message = "userId not be negative") Long userId) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("unfollowed")
                .data(followService.unfollow(userId))
                .build();
    }
}
