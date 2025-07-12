package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.CreateConversationRequestDTO;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/conversation")
@RequiredArgsConstructor
@Validated
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping("/create")
    public ApiResponse createConversation(@RequestBody @Valid CreateConversationRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .message("create conversation successfully")
                .status(200)
                .data(conversationService.create(dto))
                .build();
    }

    @PatchMapping("/{id}/to-group")
    public ApiResponse updateConversation(@PathVariable long id) {
        return ApiSuccessResponse.builder()
                .message("create conversation successfully")
                .status(200)
                .data(conversationService.toGroup(id))
                .build();
    }
}
