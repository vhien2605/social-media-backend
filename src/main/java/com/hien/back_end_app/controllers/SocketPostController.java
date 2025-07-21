package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.CreateCommentRequestDTO;
import com.hien.back_end_app.dto.request.CreatePostRequestDTO;
import com.hien.back_end_app.services.SocketPostService;
import jakarta.validation.Valid;
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
public class SocketPostController {
    private final SocketPostService socketPostService;

    @MessageMapping("/post/create-post")
    public void createPost(@Valid CreatePostRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        socketPostService.createPostSocket(dto, accessor);
    }

    @MessageMapping("/post/comment-to/{postId}")
    public void commentTo(@DestinationVariable @Min(value = 0, message = "postId must not be negative") Long postId,
                          @Valid CreateCommentRequestDTO dto,
                          SimpMessageHeaderAccessor accessor
    ) {
        socketPostService.commentTo(postId, dto, accessor);
    }

    @MessageMapping("/post/reply-to/{commentId}")
    public void replyTo(@DestinationVariable @Min(value = 0, message = "postId must not be negative") Long commentId
            , @Valid CreateCommentRequestDTO dto
            , SimpMessageHeaderAccessor accessor
    ) {
        socketPostService.replyTo(commentId, dto, accessor);
    }
}
