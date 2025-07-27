package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.PostService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/post")
@Validated
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // wall post hay group post deu coi duoc
    @GetMapping("/follow-posts")
    public ApiResponse getPost(Pageable pageable) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get posts in your networking follows successfully")
                .data(postService.getPosts(pageable))
                .build();
    }

    @GetMapping("/follow-posts/advanced-filter")
    public ApiResponse getPostFilter(Pageable pageable
            , @RequestParam(required = false) String[] post
            , @RequestParam(defaultValue = "id:asc") String[] sortBy) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get follow posts with advanced filter")
                .data(postService.getPostsAdvancedFilter(pageable, post, sortBy))
                .build();
    }

    @GetMapping("/my-posts")
    public ApiResponse getMyPosts(Pageable pageable) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get my posts")
                .data(postService.getMyPosts(pageable))
                .build();
    }

    @GetMapping("/my-posts/advanced-filter")
    public ApiResponse getMyPostsFilter(Pageable pageable
            , @RequestParam(required = false) String[] post
            , @RequestParam(defaultValue = "id:asc") String[] sortBy) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get my posts with advanced filter")
                .data(postService.getMyPostsWithAdvancedFilter(pageable, post, sortBy))
                .build();
    }

    // neu la group_post-> phai o trong group moi duoc coi
    @GetMapping("/post-comments/{postId}")
    public ApiResponse getCommentFromPost(
            Pageable pageable,
            @PathVariable @Min(value = 0, message = "postId must not be negative") Long postId
    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get post comments")
                .data(postService.getPostComments(postId, pageable))
                .build();
    }


    //neu comment reply nam trong group post , thi can check user
    @GetMapping("/reply-comments/{commentId}")
    public ApiResponse getReplyComment(
            Pageable pageable,
            @PathVariable @Min(value = 0, message = "postId must not be negative") Long commentId
    ) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get reply comments")
                .data(postService.getReplyComments(commentId, pageable))
                .build();
    }
}


