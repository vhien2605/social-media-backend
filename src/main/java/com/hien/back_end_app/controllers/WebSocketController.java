package com.hien.back_end_app.controllers;

import com.hien.back_end_app.dto.request.SocketMessageDTO;
import com.hien.back_end_app.dto.response.socket.MessageResponseDTO;
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
    public void sendMessage(@DestinationVariable Integer conversationId, SocketMessageDTO request, SimpMessageHeaderAccessor accessor) {
        webSocketService.sendMessage(request, (long) conversationId, accessor);
    }
}
