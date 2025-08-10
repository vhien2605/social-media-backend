package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.NotificationService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('read_notification')")
    @GetMapping("/all-notifications")
    public ApiResponse manageNotifications(
            Pageable pageable
    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(notificationService.getAllNotifications(pageable))
                .build();
    }

    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('read_notification')")
    @GetMapping("/detail/{notificationId}")
    public ApiResponse seeDetailNotification(
            @PathVariable @Min(value = 0, message = "notificationId must not be negative") Long notificationId
    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notification detail successfully")
                .data(notificationService.seeDetail(notificationId))
                .build();
    }


    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('read_notification')")
    @GetMapping("/all-notifications/advanced-filter")
    public ApiResponse getNotificationsFilter(
            Pageable pageable,
            @RequestParam(required = false) String[] notification,
            @RequestParam(defaultValue = "id:asc") String[] sortBy

    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(notificationService.getAllNotificationsFilter(pageable, notification, sortBy))
                .build();
    }

    @GetMapping("/my-notifications")
    public ApiResponse getMyNotifications(
            Pageable pageable
    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(notificationService.getMyNotifications(pageable))
                .build();
    }

    @GetMapping("/my-notifications/advanced-filter")
    public ApiResponse getMyNotificationsFilter(
            Pageable pageable,
            @RequestParam(required = false) String[] notification,
            @RequestParam(defaultValue = "id:asc") String[] sortBy
    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get notifications successfully")
                .data(notificationService.getMyNotificationsFilter(pageable, notification, sortBy))
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
