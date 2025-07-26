package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.response.group.PostRequestResponseDTO;
import com.hien.back_end_app.entities.UploadPostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PostRequestMediaMapper.class})
public interface PostRequestMapper {
    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "createAt", target = "createAt")
    PostRequestResponseDTO toDTO(UploadPostRequest uploadPostRequest);
}
