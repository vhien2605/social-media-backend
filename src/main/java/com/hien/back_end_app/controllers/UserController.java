package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.UpdateRolesRequestDTO;
import com.hien.back_end_app.dto.request.UpdateUserInformationRequestDTO;
import com.hien.back_end_app.dto.request.UserCreationRequestDTO;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @PostMapping("/create")
    public ApiResponse createUser(@RequestBody UserCreationRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("create user successfully")
                .status(200)
                .data(userService.create(dto))
                .build();
    }

    @PatchMapping("/update")
    public ApiResponse updateUser(@RequestBody @Valid UpdateUserInformationRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("update normal information successfully")
                .status(200)
                .data(userService.update(dto))
                .build();
    }

    @PatchMapping("/update-roles")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ApiResponse updateRole(@RequestBody @Valid UpdateRolesRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("update user roles successfully")
                .status(200)
                .data(userService.updateRoles(dto))
                .build();
    }


    @GetMapping("/get-users")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ApiResponse getUsers(Pageable pageable) {
        return ApiSuccessResponse.builder()
                .message("get users pagination successfully")
                .status(200)
                .data(userService.getUsersPagination(pageable))
                .build();
    }

    @GetMapping("/get-users/advanced-filter")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ApiResponse getUsersFilter(Pageable pageable
            , @RequestParam(required = false) String[] user
            , @RequestParam(defaultValue = "id:asc") String[] sortBy) {
        return ApiSuccessResponse.builder()
                .message("get users advanced filter successfully")
                .status(200)
                .data(userService.getUsersFilter(pageable, user, sortBy))
                .build();
    }

    @GetMapping("/{userId}/detail-information")
    public ApiResponse getUserDetailInformation(
            @PathVariable @Min(value = 0) Long userId
    ) {
        return ApiSuccessResponse.builder()
                .message("get user detail information successfully")
                .status(200)
                .data(userService.getDetailInformation(userId))
                .build();
    }
}
