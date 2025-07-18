package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.CreatePostRequestDTO;
import com.hien.back_end_app.dto.request.PostMediaRequestDTO;
import com.hien.back_end_app.dto.response.message.NotificationResponseDTO;
import com.hien.back_end_app.entities.*;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.NotificationMapper;
import com.hien.back_end_app.mappers.PostMapper;
import com.hien.back_end_app.repositories.*;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SocketPostService {
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final FollowRepository followRepository;
    private final PostMediaRepository postMediaRepository;
    private final FileService fileService;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;


    @Transactional
    public void createPostSocket(CreatePostRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        String email = accessor.getUser().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postMapper.toEntity(dto);
        post.setCreatedBy(user);

        if (dto.getPostMedias().isEmpty()) {
            postRepository.save(post);
        } else {
            Set<PostMediaRequestDTO> postMedias = dto.getPostMedias();
            List<String> fileUrls = postMedias.stream()
                    .map(pm -> fileService.convertToMultipartFile(
                            pm.getName(),
                            pm.getType(),
                            pm.getBase64Data()))
                    .map(file -> fileService.uploadFile(file
                            , Objects.requireNonNull(file.getContentType())
                            , "post_media"))
                    .toList();
            Set<PostMedia> postMediaEntities = new HashSet<>();
            for (String fileUrl : fileUrls) {
                PostMedia postMedia = PostMedia.builder()
                        .fileUrl(fileUrl)
                        .post(post)
                        .build();
                postMediaEntities.add(postMedia);
            }
            post.setPostMedias(postMediaEntities);
            postRepository.save(post);
            postMediaRepository.saveAll(new ArrayList<>(postMediaEntities));
        }

        // send notication to followers
        // get followers
        List<Follow> follows = followRepository.getAllByTargetUserId(user.getId());
        List<User> followUsers = follows.stream().map(Follow::getFollowUser).toList();

        // create notification
        Notification notification = Notification.builder()
                .type(NotificationType.POST)
                .content(user.getFullName() + " just uploaded new post, let's check it out ")
                .createdBy(user)
                .post(post)
                .build();
        notificationRepository.save(notification);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);

        // send notification that post created
        for (User followUser : followUsers) {
            simpMessagingTemplate.convertAndSendToUser(followUser.getEmail(), "/queue/notifications", notificationResponseDTO);
        }
    }
}
