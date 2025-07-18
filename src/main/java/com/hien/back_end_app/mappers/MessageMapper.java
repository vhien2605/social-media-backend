package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.response.message.MessageResponseDTO;
import com.hien.back_end_app.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MediaMessageMapper.class})
public interface MessageMapper {
    @Mapping(target = "sourceId", source = "sourceUser.id")
    @Mapping(target = "conversationId", source = "conversation.id")
    MessageResponseDTO toDTO(Message message);
}
