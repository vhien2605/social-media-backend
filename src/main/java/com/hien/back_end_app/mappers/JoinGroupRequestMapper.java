package com.hien.back_end_app.mappers;

import com.hien.back_end_app.dto.response.group.JoinGroupRequestResponseDTO;
import com.hien.back_end_app.entities.JoinGroupRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JoinGroupRequestMapper {

    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "createAt", target = "createAt")
    JoinGroupRequestResponseDTO toDTO(JoinGroupRequest joinGroupRequest);
}
