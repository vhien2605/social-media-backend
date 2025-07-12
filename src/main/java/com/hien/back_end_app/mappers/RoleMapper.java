package com.hien.back_end_app.mappers;

import com.hien.back_end_app.dto.response.authority.RoleResponseDTO;
import com.hien.back_end_app.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    RoleResponseDTO toDTO(Role role);
}
