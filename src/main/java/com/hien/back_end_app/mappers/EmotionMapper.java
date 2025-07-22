package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.response.post.EmotionResponseDTO;
import com.hien.back_end_app.entities.Emotion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmotionMapper {
    EmotionResponseDTO toDTO(Emotion emotion);
}
