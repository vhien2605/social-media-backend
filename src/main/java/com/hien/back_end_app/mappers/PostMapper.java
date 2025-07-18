package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.request.CreatePostRequestDTO;
import com.hien.back_end_app.dto.response.post.PostResponseDTO;
import com.hien.back_end_app.entities.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PostMediaMapper.class})
public interface PostMapper {
    Post toEntity(CreatePostRequestDTO dto);

    PostResponseDTO toDTO(Post post);
}
