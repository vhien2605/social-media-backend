package com.hien.back_end_app.controllers;

import com.hien.back_end_app.dto.request.*;
import com.hien.back_end_app.services.SocketConversationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class SocketConversationController {
    private final SocketConversationService webSocketService;

    @MessageMapping("/message/{conversationId}")
    public void sendMessage(@DestinationVariable @Min(value = 0, message = "conversationId must not be negative") long conversationId
            , @Valid SocketMessageDTO request
            , SimpMessageHeaderAccessor accessor) {
        webSocketService.sendMessage(request, conversationId, accessor);
    }

    @MessageMapping("/message/conversation/create")
    public void createConversation(@Valid CreateConversationRequestDTO dto
            , SimpMessageHeaderAccessor accessor) {
        webSocketService.createConversation(dto, accessor);
    }

    @MessageMapping("/message/conversation/{conversationId}/to-group")
    public void updateToGroup(@DestinationVariable @Min(value = 0, message = "conversationId must not be negative") long conversationId
            , SimpMessageHeaderAccessor accessor) {
        webSocketService.changeToGroup(conversationId, accessor);
    }

    @MessageMapping("/message/add-member/{conversationId}")
    public void addMemberToConversationGroup(@DestinationVariable @Min(value = 0, message = "conversationId must not be negative") Long conversationId, @Valid SocketAddMemberRequestDTO requestDTO, SimpMessageHeaderAccessor accessor) {
        webSocketService.addMemberToConversation(requestDTO, conversationId, accessor);
    }

    @MessageMapping("/message/delete-member/{conversationId}")
    public void deleteMemberFromConversation(@DestinationVariable @Min(value = 0, message = "conversationId must not be negative") Long conversationId
            , @Valid SocketDeleteMemberRequestDTO dto
            , SimpMessageHeaderAccessor accessor) {
        webSocketService.deleteMemberFromConversation(dto, conversationId, accessor);
    }

    @MessageMapping("/message/conversation/{conversationId}/change-name")
    public void changeConversationName(@DestinationVariable @Min(value = 0, message = "conversationId must not be negative") Long conversationId
            , @Valid ChangeConversationNameRequestDTO dto
            , SimpMessageHeaderAccessor accessor) {
        webSocketService.changeConversationName(conversationId, dto, accessor);
    }
}
