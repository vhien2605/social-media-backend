package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/v1/conversation")
public class ConversationController {
    private final ConversationService conversationService;

    @GetMapping("/all-conversations")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ApiResponse getAllConversations(Pageable pageable) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get all conversations successfully")
                .data(conversationService.getAllConversations(pageable))
                .build();
    }

    @GetMapping("/my-conversations")
    public ApiResponse getMyConversations(Pageable pageable) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get my conversations successfully")
                .data(conversationService.getMyConversations(pageable))
                .build();
    }

    @GetMapping("/my-conversations/advanced-filter")
    public ApiResponse getMyConversationsWithAdvancedFilter(Pageable pageable
            , @RequestParam(value = "conversation", required = false) String[] conversation
            , @RequestParam(value = "sortBy", defaultValue = "id:asc") String[] sortBy) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get my conversations filter successfully")
                .data(conversationService.getMyConversationsWithAdvancedFilter(pageable, conversation, sortBy))
                .build();
    }

    @GetMapping("/all-conversations/advanced-filter")
    public ApiResponse getAllConversationsWithAdvancedFilter(Pageable pageable
            , @RequestParam(value = "conversation", required = false) String[] conversation
            , @RequestParam(value = "sortBy", defaultValue = "id:asc") String[] sortBy) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get all conversations filter successfully")
                .data(null)
                .build();
    }
}
