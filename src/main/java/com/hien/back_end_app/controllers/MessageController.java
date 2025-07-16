package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.MessageService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/message")
@Validated
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/get-messages/{conversationId}")
    public ApiResponse getMessageInConversationPagination(@PathVariable @Min(value = 0, message = "conversationId must not be negative") Long conversationId
            , Pageable pageable) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get messages in conversation successfully")
                .data(messageService.getMessageWithPagination(conversationId, pageable))
                .build();
    }

    @GetMapping("/get-messages/{conversationId}/advanced-filter")
    public ApiResponse getMessageInConversationWithFilter(@PathVariable @Min(value = 0, message = "conversationId must not be negative") Long conversationId
            , Pageable pageable
            , String[] message
            , String[] sortBy
    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get messages in conversation successfully")
                .data(messageService.getMessageByConversationIdWithFilter(conversationId, pageable, message, sortBy))
                .build();
    }
}
