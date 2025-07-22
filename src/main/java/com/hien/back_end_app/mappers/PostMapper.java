package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.request.CreatePostRequestDTO;
import com.hien.back_end_app.dto.response.post.PostResponseDTO;
import com.hien.back_end_app.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PostMediaMapper.class, EmotionMapper.class, UserMapper.class})
public interface PostMapper {
    Post toEntity(CreatePostRequestDTO dto);


    @Mapping(source = "createAt", target = "createAt")
    PostResponseDTO toDTO(Post post);
}
