package com.hien.back_end_app.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hien.back_end_app.dto.request.AlbumCreateRequestDTO;
import com.hien.back_end_app.dto.request.UpdateRolesRequestDTO;
import com.hien.back_end_app.dto.request.UpdateUserInformationRequestDTO;
import com.hien.back_end_app.dto.request.UserCreationRequestDTO;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.services.UserService;
import com.hien.back_end_app.utils.enums.ErrorCode;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @PostMapping("/create")
    public ApiResponse createUser(
            @RequestParam(name = "image", required = false) MultipartFile image
            , @RequestParam(name = "data") String dto) {
        try {
            UserCreationRequestDTO jsonDTO = objectMapper.readValue(dto, UserCreationRequestDTO.class);
            return ApiSuccessResponse.builder()
                    .message("create user successfully")
                    .status(200)
                    .data(userService.create(jsonDTO, image))
                    .build();
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.JSON_INVALID);
        }
    }

    @PatchMapping("/update")
    public ApiResponse updateUser(
            @RequestParam(name = "image", required = false) MultipartFile image,
            @RequestParam(name = "data") String dto) {
        try {
            UpdateUserInformationRequestDTO jsonDTO = objectMapper.readValue(dto, UpdateUserInformationRequestDTO.class);
            return ApiSuccessResponse.builder()
                    .message("update normal information successfully")
                    .status(200)
                    .data(userService.update(jsonDTO, image))
                    .build();
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.JSON_INVALID);
        }
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

    @PostMapping("/album/create-album")
    public ApiResponse createOwnedAlbum(
            @RequestParam(name = "images") List<MultipartFile> images,
            @RequestParam(name = "data") String dto
    ) {
        try {
            AlbumCreateRequestDTO jsonDTO = objectMapper.readValue(dto, AlbumCreateRequestDTO.class);
            return ApiSuccessResponse.builder()
                    .message("update user roles successfully")
                    .status(200)
                    .data(userService.createAlbum(jsonDTO, images))
                    .build();
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.JSON_INVALID);
        }
    }

    @GetMapping("/{userId}/album/get-albums")
    public ApiResponse getUserAlbums(
            Pageable pageable,
            @PathVariable @Min(value = 0, message = "user must be greater than 0") Long userId
    ) {
        return ApiSuccessResponse.builder()
                .message("get albums successfully")
                .status(200)
                .data(userService.getUserAlbums(userId, pageable))
                .build();
    }
}
