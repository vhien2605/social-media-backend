package com.hien.back_end_app.controllers;


import com.hien.back_end_app.services.SocketFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SocketFollowController {
    private final SocketFollowService socketFollowService;

    @MessageMapping("/follow/follow-to/{userId}")
    public void followTo(@DestinationVariable Long userId, SimpMessageHeaderAccessor accessor) {
        socketFollowService.followTo(userId, accessor);
    }
}
