package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.AddMemberRequestDTO;
import com.hien.back_end_app.dto.request.CheckJoinRequestDTO;
import com.hien.back_end_app.dto.request.CheckPostRequestDTO;
import com.hien.back_end_app.dto.request.JoinGroupRequestDTO;
import com.hien.back_end_app.dto.response.message.NotificationResponseDTO;
import com.hien.back_end_app.entities.*;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.NotificationMapper;
import com.hien.back_end_app.repositories.*;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.NotificationType;
import com.hien.back_end_app.utils.enums.PostType;
import com.hien.back_end_app.utils.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocketGroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupUserRepository groupUserRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final JoinGroupRequestRepository joinGroupRequestRepository;
    private final UploadPostRequestRepository uploadPostRequestRepository;
    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final ReceiverNotificationRepository receiverNotificationRepository;

    @Transactional
    public void addMember(AddMemberRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        long groupId = dto.getGroupId();
        long addedUserId = dto.getAddedUserId();
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_EXIST));
        String email = accessor.getUser().getName();
        if (!group.getCreatedBy().getEmail().equals(email)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        User addedUser = userRepository.findById(addedUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // if user is already in group,throw exception
        Optional<GroupUser> checkedGroupUser = groupUserRepository.findByGroupIdAndUserId(groupId, addedUserId);
        if (checkedGroupUser.isPresent()) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_GROUP);
        }
        Role groupRole = roleRepository.findByName("GROUP_USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));
        GroupUser groupUser = GroupUser.builder()
                .user(addedUser)
                .role(groupRole)
                .group(group)
                .build();
        groupUserRepository.save(groupUser);


        // create and send notification
        Notification notification = Notification.builder()
                .content("you are added to group " + group.getName() + " let's check it out")
                .createdBy(group.getCreatedBy())
                .type(NotificationType.GROUP_INVITE)
                .group(group)
                .build();
        notificationRepository.save(notification);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);

        ReceiverNotification receiverNotification = ReceiverNotification.builder()
                .receiverUser(addedUser)
                .notification(notification)
                .build();
        receiverNotificationRepository.save(receiverNotification);

        simpMessagingTemplate.convertAndSendToUser(addedUser.getEmail(), "/queue/notifications", notificationResponseDTO);
    }

    @Transactional
    public void createJoinRequest(JoinGroupRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        String email = accessor.getUser().getName();
        long groupId = dto.getGroupId();
        String message = dto.getMessage();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_EXIST));
        User groupOwnedUser = group.getCreatedBy();

        User requestUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // if user is in group, throw exception
        Optional<GroupUser> checkedGroupUser = groupUserRepository.findByGroupIdAndUserId(group.getId(), requestUser.getId());
        if (checkedGroupUser.isPresent()) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_GROUP);
        }


        JoinGroupRequest joinGroupRequest = JoinGroupRequest.builder()
                .createdBy(requestUser)
                .group(group)
                .message(message)
                .status(RequestStatus.PENDING)
                .build();
        joinGroupRequestRepository.save(joinGroupRequest);


        Notification notification = Notification.builder()
                .content("you received join request to group " + group.getName() + " from " + requestUser.getFullName())
                .type(NotificationType.GROUP_JOIN)
                .group(group)
                .createdBy(requestUser)
                .build();
        notificationRepository.save(notification);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);

        ReceiverNotification receiverNotification = ReceiverNotification.builder()
                .receiverUser(groupOwnedUser)
                .notification(notification)
                .build();
        receiverNotificationRepository.save(receiverNotification);

        simpMessagingTemplate.convertAndSendToUser(groupOwnedUser.getEmail(), "/queue/notifications", notificationResponseDTO);
    }

    @Transactional
    public void checkJoinRequest(Long joinRequestId, CheckJoinRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        boolean accepted = dto.isAccepted();
        String adminEmail = accessor.getUser().getName();
        JoinGroupRequest joinGroupRequest = joinGroupRequestRepository.findById(joinRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_EXIST));

        User groupOwnedUser = joinGroupRequest.getGroup().getCreatedBy();
        User requestUser = joinGroupRequest.getCreatedBy();
        Group group = joinGroupRequest.getGroup();
        if (!groupOwnedUser.getEmail().equals(adminEmail)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        String notificationContent = (accepted) ? "you are accepted" : "you are rejected";
        if (accepted) {
            // update status
            joinGroupRequest.setStatus(RequestStatus.ACCEPTED);

            // if user already in group , throw exception
            Optional<GroupUser> checkedGroupUser = groupUserRepository.findByGroupIdAndUserId(group.getId(), requestUser.getId());
            if (checkedGroupUser.isPresent()) {
                throw new AppException(ErrorCode.USER_ALREADY_IN_GROUP);
            }

            // add groupUser
            Role groupRole = roleRepository.findByName("GROUP_USER")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));
            GroupUser groupUser = GroupUser.builder()
                    .user(requestUser)
                    .role(groupRole)
                    .group(group)
                    .build();
            groupUserRepository.save(groupUser);
        } else {
            // update status and do nothing
            joinGroupRequest.setStatus(RequestStatus.REJECTED);
        }
        joinGroupRequestRepository.save(joinGroupRequest);

        // send to requestUser to alert they are accepted or rejected
        Notification notification = Notification.builder()
                .content(notificationContent)
                .type(NotificationType.GROUP_JOIN)
                .group(group)
                .createdBy(groupOwnedUser)
                .build();
        notificationRepository.save(notification);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);

        ReceiverNotification receiverNotification = ReceiverNotification.builder()
                .receiverUser(requestUser)
                .notification(notification)
                .build();
        receiverNotificationRepository.save(receiverNotification);

        simpMessagingTemplate.convertAndSendToUser(requestUser.getEmail(), "/queue/notifications", notificationResponseDTO);
    }

    @Transactional
    public void checkPostRequest(Long postRequestId, CheckPostRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        boolean accepted = dto.isAccepted();
        String email = accessor.getUser().getName();
        UploadPostRequest uploadPostRequest = uploadPostRequestRepository.findByIdWithReferences(postRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_EXIST));
        User adminGroup = uploadPostRequest.getGroup().getCreatedBy();
        User requestUser = uploadPostRequest.getCreatedBy();
        User adminUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // check authority
        if (adminUser.getId() != adminGroup.getId()) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        if (accepted) {
            // update to accepted
            uploadPostRequest.setStatus(RequestStatus.ACCEPTED);
            uploadPostRequestRepository.save(uploadPostRequest);
            // create group post
            if (uploadPostRequest.getMedias() == null || uploadPostRequest.getMedias().isEmpty()) {
                Post post = Post.builder()
                        .type(PostType.GROUP_POST)
                        .group(uploadPostRequest.getGroup())
                        .createdBy(requestUser)
                        .content(uploadPostRequest.getContent())
                        .build();
                postRepository.save(post);
            } else {
                Set<PostRequestMedia> postRequestMedias = uploadPostRequest.getMedias();
                Post post = Post.builder()
                        .type(PostType.GROUP_POST)
                        .group(uploadPostRequest.getGroup())
                        .createdBy(requestUser)
                        .content(uploadPostRequest.getContent())
                        .build();
                postRepository.save(post);
                Set<PostMedia> postMedias = postRequestMedias.stream()
                        .map(pr -> PostMedia.builder()
                                .fileUrl(pr.getFileUrl())
                                .build())
                        .peek(pm -> pm.setPost(post))
                        .collect(Collectors.toSet());
                post.setPostMedias(postMedias);
                postMediaRepository.saveAll(postMedias);
            }
        } else {
            // update to rejected
            uploadPostRequest.setStatus(RequestStatus.REJECTED);
            uploadPostRequestRepository.save(uploadPostRequest);
        }

        // send notification
        String notificationContent = (accepted) ? "your post is accepted" : "your post is rejected ";
        Notification notification = Notification.builder()
                .content(notificationContent)
                .group(uploadPostRequest.getGroup())
                .type(NotificationType.GROUP_POST)
                .createdBy(adminGroup)
                .build();
        notificationRepository.save(notification);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);

        ReceiverNotification receiverNotification = ReceiverNotification.builder()
                .receiverUser(requestUser)
                .notification(notification)
                .build();
        receiverNotificationRepository.save(receiverNotification);

        simpMessagingTemplate.convertAndSendToUser(requestUser.getEmail(), "/queue/notifications", notificationResponseDTO);
    }
}
