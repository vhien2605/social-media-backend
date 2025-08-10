package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('read_notification')")
    @GetMapping("/all-notifications")
    public ApiResponse manageNotifications(
            Pageable pageable
    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(null)
                .build();
    }


    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('read_notification')")
    public ApiResponse getNotificationsFilter(
            Pageable pageable,
            @RequestParam(required = false) String[] notification,
            @RequestParam(defaultValue = "id:asc") String[] sortBy

    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(null)
                .build();
    }

    public ApiResponse getMyNotifications() {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(null)
                .build();
    }

    public ApiResponse getMyNotificationsFilter() {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(null)
                .build();
    }

    public ApiResponse getGeneralNotifications() {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(null)
                .build();
    }

    public ApiResponse getGeneralNotificationsFilter() {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(null)
                .build();
    }
}
