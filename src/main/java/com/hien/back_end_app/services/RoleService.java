package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.PermissionRequestDTO;
import com.hien.back_end_app.dto.request.RoleRequestDTO;
import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.authority.PermissionResponseDTO;
import com.hien.back_end_app.dto.response.authority.RoleResponseDTO;
import com.hien.back_end_app.entities.Permission;
import com.hien.back_end_app.entities.Role;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.PermissionMapper;
import com.hien.back_end_app.mappers.RoleMapper;
import com.hien.back_end_app.repositories.PermissionRepository;
import com.hien.back_end_app.repositories.RoleRepository;
import com.hien.back_end_app.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;


    @Transactional
    public RoleResponseDTO createRole(RoleRequestDTO dto) {
        String name = dto.getName();
        var roleWrapper = roleRepository.findByName(name);
        if (roleWrapper.isPresent()) {
            throw new AppException(ErrorCode.ROLE_EXIST);
        }
        Set<PermissionRequestDTO> permissionRequestDTOS = dto.getPermissions();
        List<String> dtoPermissionNames = permissionRequestDTOS.stream()
                .map(PermissionRequestDTO::getName).toList();
        // find existed permissions
        Set<Permission> permissions = new HashSet<>();
        List<Permission> existedPermissions = permissionRepository.findAllInNames(dtoPermissionNames);
        List<String> existedPermissionNames = existedPermissions.stream().map(Permission::getName).toList();
        List<Permission> nonExistedPermissions = permissionRequestDTOS
                .stream().filter(p -> !existedPermissionNames.contains(p.getName()))
                .map(permissionMapper::toEntity)
                .toList();
        permissions.addAll(existedPermissions);
        permissions.addAll(nonExistedPermissions);

        Role role = Role.builder()
                .name(name)
                .description(dto.getDescription())
                .permissions(permissions)
                .build();
        permissionRepository.saveAll(nonExistedPermissions);
        roleRepository.save(role);
        return roleMapper.toDTO(role);
    }


    @Transactional
    public RoleResponseDTO updateRole(Long roleId, RoleRequestDTO dto) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));

        Set<PermissionRequestDTO> permissionRequestDTOS = dto.getPermissions();
        List<String> dtoPermissionNames = permissionRequestDTOS.stream()
                .map(PermissionRequestDTO::getName).toList();
        // find existed permissions
        Set<Permission> permissions = new HashSet<>();
        List<Permission> existedPermissions = permissionRepository.findAllInNames(dtoPermissionNames);
        List<String> existedPermissionNames = existedPermissions.stream().map(Permission::getName).toList();
        List<Permission> nonExistedPermissions = permissionRequestDTOS
                .stream().filter(p -> !existedPermissionNames.contains(p.getName()))
                .map(permissionMapper::toEntity)
                .toList();
        permissions.addAll(existedPermissions);
        permissions.addAll(nonExistedPermissions);

        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setPermissions(permissions);

        permissionRepository.saveAll(nonExistedPermissions);
        roleRepository.save(role);
        return roleMapper.toDTO(role);
    }

    public PageResponseDTO<Object> readRoles(Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(pageable);
        List<Long> roleIds = roles.stream()
                .map(Role::getId)
                .toList();
        List<Role> fetchedRoles = roleRepository.findWithIdsWithPermissions(roleIds);
        Map<Long, Role> idRoleMap = fetchedRoles.stream()
                .collect(Collectors.toMap(Role::getId, f -> f));
        List<RoleResponseDTO> dtos = roleIds.stream()
                .map(idRoleMap::get)
                .map(roleMapper::toDTO)
                .toList();
        return PageResponseDTO.builder()
                .data(dtos)
                .totalPage(roles.getTotalPages())
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .build();
    }


    public PermissionResponseDTO updatePermission(Long permissionId, PermissionRequestDTO dto) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXIST));
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        permissionRepository.save(permission);
        return permissionMapper.toDTO(permission);
    }


    @Transactional
    public PermissionResponseDTO createPermission(PermissionRequestDTO dto) {
        var permissionWrapper = permissionRepository.findByName(dto.getName());
        if (permissionWrapper.isPresent()) {
            throw new AppException(ErrorCode.PERMISSION_EXIST);
        }

        Permission permission = Permission.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        permissionRepository.save(permission);
        return permissionMapper.toDTO(permission);
    }
}
