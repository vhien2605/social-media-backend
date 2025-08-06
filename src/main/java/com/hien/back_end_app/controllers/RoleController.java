package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.PermissionRequestDTO;
import com.hien.back_end_app.dto.request.RoleRequestDTO;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/role")
@Validated
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('create_role')")
    @PostMapping("/create-role")
    public ApiResponse createRole(@RequestBody @Valid RoleRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("created role")
                .data(roleService.createRole(dto))
                .build();
    }

    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('update_role')")
    @PatchMapping("/update-role/{roleId}")
    public ApiResponse updateRole(
            @PathVariable @Min(value = 0, message = "role is must not be negative") Long roleId
            , @RequestBody @Valid RoleRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("updated role")
                .data(roleService.updateRole(roleId, dto))
                .build();
    }

    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('read_role')")
    @GetMapping("/read-roles")
    public ApiResponse getAllRoles(Pageable pageable) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("read roles")
                .data(roleService.readRoles(pageable))
                .build();
    }

    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('update_permission')")
    @PatchMapping("/permission/update-permission/{permissionId}")
    public ApiResponse updatePermission(
            @PathVariable @Min(value = 0, message = "permission must not") Long permissionId,
            @RequestBody @Valid PermissionRequestDTO dto
    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("update permission")
                .data(roleService.updatePermission(permissionId, dto))
                .build();
    }

    @PreAuthorize("hasRole('SYS_ADMIN') OR hasAuthority('create_permission')")
    @PostMapping("/permission/create-permission")
    public ApiResponse createPermission(@RequestBody @Valid PermissionRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("create permission")
                .data(roleService.createPermission(dto))
                .build();
    }
}
