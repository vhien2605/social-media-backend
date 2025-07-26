package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.response.group.PostRequestMediaResponseDTO;
import com.hien.back_end_app.entities.PostRequestMedia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostRequestMediaMapper {
    PostRequestMediaResponseDTO toDTO(PostRequestMedia postRequestMedia);
}
