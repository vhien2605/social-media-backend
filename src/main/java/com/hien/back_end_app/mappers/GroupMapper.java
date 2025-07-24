package com.hien.back_end_app.mappers;

import com.hien.back_end_app.dto.request.CreateGroupRequestDTO;
import com.hien.back_end_app.dto.response.group.GroupResponseDTO;
import com.hien.back_end_app.entities.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    Group toEntity(CreateGroupRequestDTO dto);


    @Mapping(source = "createdBy.id", target = "createdId")
    GroupResponseDTO toDTO(Group group);
}
