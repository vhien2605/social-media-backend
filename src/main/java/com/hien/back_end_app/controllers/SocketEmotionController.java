package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.EmotionRequestDTO;
import com.hien.back_end_app.services.SocketEmotionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class SocketEmotionController {
    private final SocketEmotionService socketEmotionService;

    @MessageMapping("/emotion/post-emotion-at/{postId}")
    public void createPostEmotion(@DestinationVariable @Min(value = 0, message = "postId must be greater than 0") Long postId
            , @Valid EmotionRequestDTO dto
            , SimpMessageHeaderAccessor accessor
    ) {
        socketEmotionService.createPostEmotion(postId, dto, accessor);
    }

    @MessageMapping("/emotion/comment-emotion-at/{commentId}")
    public void createCommentEmotion(
            @DestinationVariable @Min(value = 0, message = "commentId must not be null") Long commentId
            , @Valid EmotionRequestDTO dto,
            SimpMessageHeaderAccessor accessor
    ) {
        socketEmotionService.createCommentEmotion(commentId, dto, accessor);
    }
}
