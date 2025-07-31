package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.request.PermissionRequestDTO;
import com.hien.back_end_app.dto.response.authority.PermissionResponseDTO;
import com.hien.back_end_app.entities.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponseDTO toDTO(Permission permission);

    Permission toEntity(PermissionRequestDTO dto);
}
