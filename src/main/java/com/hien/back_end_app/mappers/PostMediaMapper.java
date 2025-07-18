package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.request.PostMediaRequestDTO;
import com.hien.back_end_app.dto.response.post.PostMediaResponseDTO;
import com.hien.back_end_app.entities.PostMedia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMediaMapper {
    PostMedia toEntity(PostMediaRequestDTO dto);

    PostMediaResponseDTO toDTO(PostMedia postMedia);
}
