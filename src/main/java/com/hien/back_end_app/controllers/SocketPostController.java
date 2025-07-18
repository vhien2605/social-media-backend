package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.CreatePostRequestDTO;
import com.hien.back_end_app.services.SocketPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SocketPostController {
    private final SocketPostService socketPostService;

    @MessageMapping("/post/create-post")
    public void createPost(CreatePostRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        socketPostService.createPostSocket(dto, accessor);
    }
}
