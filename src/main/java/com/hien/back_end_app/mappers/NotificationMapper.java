package com.hien.back_end_app.mappers;

import com.hien.back_end_app.dto.response.message.NotificationResponseDTO;
import com.hien.back_end_app.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "commentId", source = "comment.id")
    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "emotionId", source = "emotion.id")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "updateAt", source = "updateAt")
    @Mapping(target = "createAt", source = "createAt")
    NotificationResponseDTO toDTO(Notification notification);
}
