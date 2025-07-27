package com.hien.back_end_app.mappers;

import com.hien.back_end_app.dto.response.post.CommentResponseDTO;
import com.hien.back_end_app.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmotionMapper.class})
public interface CommentMapper {
    @Mapping(source = "createdBy.id", target = "createdBy")
    CommentResponseDTO toDTO(Comment comment);
}
