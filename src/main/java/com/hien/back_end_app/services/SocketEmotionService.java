package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.EmotionRequestDTO;
import com.hien.back_end_app.dto.response.message.NotificationResponseDTO;
import com.hien.back_end_app.entities.*;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.NotificationMapper;
import com.hien.back_end_app.repositories.*;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocketEmotionService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final GroupUserRepository groupUserRepository;
    private final EmotionRepository emotionRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CommentRepository commentRepository;

    @Transactional
    public void createPostEmotion(Long postId, EmotionRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        String email = accessor.getUser().getName();
        User createdUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post targetPost = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        User ownedPostUser = targetPost.getCreatedBy();
        Group postGroup = targetPost.getGroup();

        // check if group-> check that user must be in group
        if (postGroup != null) {
            Optional<GroupUser> groupUser = groupUserRepository.findByGroupIdAndUserId(postGroup.getId(), createdUser.getId());
            if (groupUser.isEmpty()) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        }

        // if user dropped emotion before, change type
        Optional<Emotion> existedEmotion = emotionRepository
                .findByCreatedIdAndPostId(createdUser.getId(), targetPost.getId());

        if (existedEmotion.isEmpty()) {
            Emotion emotion = Emotion.builder()
                    .type(dto.getType())
                    .post(targetPost)
                    .createdBy(createdUser)
                    .build();
            // save
            emotionRepository.save(emotion);


            if (createdUser.getId() != ownedPostUser.getId()) {
                Notification notification = Notification.builder()
                        .createdBy(createdUser)
                        .type(NotificationType.EMOTION)
                        .content(createdUser.getFullName() + " took emotion " + emotion.getType().name() + " to your post")
                        .post(targetPost)
                        .group(postGroup)
                        .emotion(emotion)
                        .build();
                notificationRepository.save(notification);
                NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);
                simpMessagingTemplate.convertAndSendToUser(ownedPostUser.getEmail(), "/queue/notifications", notificationResponseDTO);
            }
        } else {
            Emotion existed = existedEmotion.get();
            existed.setType(dto.getType());
            // save
            emotionRepository.save(existed);
        }
    }

    @Transactional
    public void createCommentEmotion(Long commentId, EmotionRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        String email = accessor.getUser().getName();
        User createdUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Comment targetComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXIST));
        User ownedCommentUser = targetComment.getCreatedBy();
        Group commentGroup = targetComment.getPost().getGroup();
        Post targetPost = targetComment.getPost();

        if (commentGroup != null) {
            Optional<GroupUser> groupUser = groupUserRepository.findByGroupIdAndUserId(commentGroup.getId(), createdUser.getId());
            if (groupUser.isEmpty()) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        }

        Optional<Emotion> existedEmotion = emotionRepository.findByCreatedIdAndCommentId(createdUser.getId(), commentId);
        if (existedEmotion.isEmpty()) {
            Emotion emotion = Emotion.builder()
                    .type(dto.getType())
                    .comment(targetComment)
                    .createdBy(createdUser)
                    .build();

            emotionRepository.save(emotion);

            if (createdUser.getId() != ownedCommentUser.getId()) {
                Notification notification = Notification.builder()
                        .createdBy(createdUser)
                        .type(NotificationType.EMOTION)
                        .content(createdUser.getFullName() + " took emotion " + emotion.getType().name() + " to your comment")
                        .group(commentGroup)
                        .comment(targetComment)
                        .emotion(emotion)
                        .build();
                notificationRepository.save(notification);

                NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);
                simpMessagingTemplate.convertAndSendToUser(ownedCommentUser.getEmail(), "/queue/notifications", notificationResponseDTO);
            }
        } else {
            Emotion existed = existedEmotion.get();
            existed.setType(dto.getType());
            emotionRepository.save(existed);
        }
    }
}
