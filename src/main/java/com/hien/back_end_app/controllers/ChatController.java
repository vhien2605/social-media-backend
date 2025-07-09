package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @PostMapping("/conversation/create")
    public ApiResponse createConversation() {
        return null;
    }

    @GetMapping("/conversation/my-conversations")
    public ApiResponse getMyConversations() {
        return null;
    }
}
