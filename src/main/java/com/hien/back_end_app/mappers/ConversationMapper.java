package com.hien.back_end_app.mappers;

import com.hien.back_end_app.dto.response.conversation.ConversationInformationResponseDTO;
import com.hien.back_end_app.dto.response.conversation.ConversationResponseDTO;
import com.hien.back_end_app.entities.Conversation;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {UserMapper.class, RoleMapper.class, PermissionMapper.class})
public interface ConversationMapper {
    ConversationResponseDTO toDTO(Conversation conversation);

    ConversationInformationResponseDTO toDetailDTO(Conversation conversation);
}
