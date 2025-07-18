package com.hien.back_end_app.mappers;

import com.hien.back_end_app.dto.response.message.MediaResponseDTO;
import com.hien.back_end_app.entities.MessageMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MediaMessageMapper {
    @Mapping(target = "fileUrl", source = "fileUrl")
    MediaResponseDTO toDTO(MessageMedia messageMedia);
}
