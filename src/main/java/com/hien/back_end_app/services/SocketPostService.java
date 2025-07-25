package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.*;
import com.hien.back_end_app.dto.response.message.NotificationResponseDTO;
import com.hien.back_end_app.entities.*;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.NotificationMapper;
import com.hien.back_end_app.mappers.PostMapper;
import com.hien.back_end_app.repositories.*;
import com.hien.back_end_app.utils.enums.*;
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
    private final CommentRepository commentRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final UploadPostRequestRepository uploadPostRequestRepository;
    private final PostRequestMediaRepository postRequestMediaRepository;


    @Transactional
    public void createPostSocket(CreatePostRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        String email = accessor.getUser().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postMapper.toEntity(dto);
        post.setCreatedBy(user);
        post.setType(PostType.WALL_POST);

        if (dto.getPostMedias() == null || dto.getPostMedias().isEmpty()) {
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


    /**
     * Create comment to post method
     *
     * @param postId   post's id that user comment to
     * @param dto      comment properties
     * @param accessor socket header
     */
    public void commentTo(Long postId, CreateCommentRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));

        String createdEmail = accessor.getUser().getName();
        User createdUser = userRepository.findByEmailWithNoReferences(createdEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String commentContent = dto.getContent();
        Comment comment = Comment.builder()
                .content(commentContent)
                .createdBy(createdUser)
                .post(post)
                .replyTo(null)
                .type(CommentType.POST_COMMENT)
                .build();
        commentRepository.save(comment);

        //create and send notification to user has the post
        User postCreatedUser = post.getCreatedBy();
        Notification notification = Notification.builder()
                .createdBy(createdUser)
                .comment(comment)
                .post(post)
                .type(NotificationType.COMMENT_POST)
                .content(createdUser.getFullName() + " just commented to your post. Let's reply them")
                .build();
        notificationRepository.save(notification);

        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);

        // send alert to userPost
        simpMessagingTemplate.convertAndSendToUser(postCreatedUser.getEmail(), "/queue/notifications", notificationResponseDTO);
    }

    public void replyTo(Long commentId, CreateCommentRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        String createdEmail = accessor.getUser().getName();
        User createdUser = userRepository.findByEmailWithNoReferences(createdEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Comment targetComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXIST));
        Post targetPost = targetComment.getPost();
        User targetCommentUser = targetComment.getCreatedBy();

        Comment replyComment = Comment.builder()
                .content(dto.getContent())
                .createdBy(createdUser)
                .replyTo(targetComment)
                .post(targetPost)
                .type(CommentType.REPLY_COMMENT)
                .build();
        commentRepository.save(replyComment);


        //create and send notifications
        // if I reply to myself, not send notification
        if (targetCommentUser.getId() != createdUser.getId()) {
            Notification notification = Notification.builder()
                    .type(NotificationType.COMMENT_REPLY)
                    .content(createdUser.getFullName() + " just replied to your comment,Let's check it out")
                    .createdBy(createdUser)
                    .comment(replyComment)
                    .post(targetPost)
                    .build();
            notificationRepository.save(notification);

            NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);
            simpMessagingTemplate.convertAndSendToUser(targetCommentUser.getEmail(), "/queue/notifications", notificationResponseDTO);
        }
    }
    // create group post-> create request and split other route because need group_id
    // comment to group post-> check role because it does need group_id
    // reply to group comment-> check role --------------------------


    @Transactional
    public void createGroupPostSocket(CreateGroupPostRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        String createdEmail = accessor.getUser().getName();
        String content = dto.getContent();
        long groupId = dto.getGroupId();
        Group group = groupRepository.findByIdWithGroupUsers(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_EXIST));
        Set<GroupUser> groupUsers = group.getGroupUsers();
        User postUser = userRepository.findByEmailWithNoReferences(createdEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // check user is member in group
        boolean isInGroup = groupUsers.stream().anyMatch(gu -> gu.getUser().getId() == postUser.getId());
        if (!isInGroup) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        if (group.getCreatedBy().getId() == postUser.getId()) {
            // if user is creator of group-> can upload directly
            Post post = Post.builder()
                    .content(content)
                    .group(group)
                    .createdBy(postUser)
                    .type(PostType.GROUP_POST)
                    .build();
            if (dto.getMedias() == null || dto.getMedias().isEmpty()) {
                postRepository.save(post);
            } else {
                Set<GroupPostMediaDTO> postMedias = dto.getMedias();
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
            // send notifications to all groupUsers
            Notification notification = Notification.builder()
                    .type(NotificationType.GROUP_POST)
                    .createdBy(postUser)
                    .group(group)
                    .post(post)
                    .content(postUser.getFullName() + " released new post in group " + group.getName() + " .Let's check it out")
                    .build();
            notificationRepository.save(notification);
            NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);
            List<User> usersInGroup = groupUsers.stream().map(GroupUser::getUser)
                    .toList();
            for (User u : usersInGroup) {
                simpMessagingTemplate.convertAndSendToUser(u.getEmail(), "/queue/notifications", notificationResponseDTO);
            }
        } else {
            // if not,user must create upload request to admin
            UploadPostRequest uploadPostRequest = UploadPostRequest.builder()
                    .createdBy(postUser)
                    .status(RequestStatus.PENDING)
                    .group(group)
                    .content(content)
                    .build();
            if (dto.getMedias() == null || dto.getMedias().isEmpty()) {
                uploadPostRequestRepository.save(uploadPostRequest);
            } else {
                Set<GroupPostMediaDTO> postMedias = dto.getMedias();
                List<String> fileUrls = postMedias.stream()
                        .map(pm -> fileService.convertToMultipartFile(
                                pm.getName(),
                                pm.getType(),
                                pm.getBase64Data()))
                        .map(file -> fileService.uploadFile(file
                                , Objects.requireNonNull(file.getContentType())
                                , "post_media"))
                        .toList();
                Set<PostRequestMedia> postMediaEntities = new HashSet<>();
                for (String fileUrl : fileUrls) {
                    PostRequestMedia postRequestMedia = PostRequestMedia.builder()
                            .fileUrl(fileUrl)
                            .postRequest(uploadPostRequest)
                            .build();
                    postMediaEntities.add(postRequestMedia);
                }
                uploadPostRequest.setMedias(postMediaEntities);
                uploadPostRequestRepository.save(uploadPostRequest);
                postRequestMediaRepository.saveAll(new ArrayList<>(postMediaEntities));
            }

            // send notification to admin group
            Notification notification = Notification.builder()
                    .content(postUser.getFullName() + " created new post request,check it for acceptance or reject")
                    .type(NotificationType.GROUP_POST)
                    .createdBy(postUser)
                    .group(group)
                    .build();
            notificationRepository.save(notification);
            NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);
            simpMessagingTemplate.convertAndSendToUser(group.getCreatedBy().getEmail(), "/queue/notifications", notificationResponseDTO);
        }
    }
}
