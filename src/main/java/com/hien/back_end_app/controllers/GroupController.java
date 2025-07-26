package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.CreateGroupRequestDTO;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.GroupService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/group")
@Validated
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/create-group")
    public ApiResponse createGroup(@RequestBody @Valid CreateGroupRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("create group successfully")
                .data(groupService.createGroup(dto))
                .build();
    }

    @GetMapping("/group-posts/{groupId}")
    public ApiResponse groupPost(Pageable pageable, @PathVariable @Min(value = 0, message = "groupId must not be negative") Long groupId) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get group posts successfully")
                .data(groupService.groupPosts(groupId, pageable))
                .build();
    }

    @GetMapping("/request-posts/{groupId}")
    public ApiResponse requestPost(Pageable pageable, @PathVariable @Min(value = 0, message = "groupId must not be negative") Long groupId) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get request post successfully")
                .data(groupService.requestPosts(groupId, pageable))
                .build();
    }

    @GetMapping("/join-requests")
    public ApiResponse joinRequest(Pageable pageable, @PathVariable @Min(value = 0, message = "groupId must not be negative") Long groupId) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get join request successfully")
                .data(groupService.joinRequests(groupId, pageable))
                .build();
    }
}
