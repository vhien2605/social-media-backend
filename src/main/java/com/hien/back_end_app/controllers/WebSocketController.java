package com.hien.back_end_app.controllers;

import com.hien.back_end_app.dto.request.SocketAddMemberRequestDTO;
import com.hien.back_end_app.dto.request.SocketMessageDTO;
import com.hien.back_end_app.services.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final WebSocketService webSocketService;

    @MessageMapping("/message/{conversationId}")
    public void sendMessage(@DestinationVariable long conversationId, SocketMessageDTO request, SimpMessageHeaderAccessor accessor) {
        webSocketService.sendMessage(request, conversationId, accessor);
    }

    @MessageMapping("/message/add-member/{conversationId}")
    public void addMemberToConversationGroup(@DestinationVariable Long conversationId, SocketAddMemberRequestDTO requestDTO, SimpMessageHeaderAccessor accessor) {
        webSocketService.addMemberToConversation(requestDTO, conversationId, accessor);
    }
}
