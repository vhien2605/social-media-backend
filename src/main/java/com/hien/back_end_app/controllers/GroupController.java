package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.CreateGroupRequestDTO;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
