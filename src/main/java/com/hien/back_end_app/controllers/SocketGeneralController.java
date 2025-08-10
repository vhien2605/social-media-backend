package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.GeneralNotificationRequestDTO;
import com.hien.back_end_app.services.SocketGeneralService;
import com.hien.back_end_app.utils.anotations.PreSocketAuthorize;
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
public class SocketGeneralController {
    private final SocketGeneralService socketGeneralService;

    @PreSocketAuthorize(authorities = {"ROLE_SYS_ADMIN", "send_general_message"})
    @MessageMapping("/general/broadcast")
    public void broadcastMessage(GeneralNotificationRequestDTO dto,
                                 SimpMessageHeaderAccessor accessor
    ) {
        socketGeneralService.broadcast(dto, accessor);
    }

    @PreSocketAuthorize(authorities = {"ROLE_SYS_ADMIN", "send_general_message"})
    @MessageMapping("/general/to-user/{userId}")
    public void sendToUser(@DestinationVariable @Min(value = 0) Long userId,
                           GeneralNotificationRequestDTO dto,
                           SimpMessageHeaderAccessor accessor
    ) {
        socketGeneralService.sendToUser(userId, dto, accessor);
    }
}
