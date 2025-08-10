package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.response.message.NotificationResponseDTO;
import com.hien.back_end_app.entities.Follow;
import com.hien.back_end_app.entities.Notification;
import com.hien.back_end_app.entities.ReceiverNotification;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.NotificationMapper;
import com.hien.back_end_app.repositories.FollowRepository;
import com.hien.back_end_app.repositories.NotificationRepository;
import com.hien.back_end_app.repositories.ReceiverNotificationRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocketFollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ReceiverNotificationRepository receiverNotificationRepository;

    @Transactional
    public void followTo(Long userId, SimpMessageHeaderAccessor accessor) {
        String followEmail = accessor.getUser().getName();
        User followUser = userRepository.findByEmailWithNoReferences(followEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Follow follow = Follow.builder()
                .followUser(followUser)
                .targetUser(targetUser)
                .build();
        followRepository.save(follow);
        // create notification and send to target user

        Notification notification = Notification.builder()
                .type(NotificationType.FOLLOW)
                .content(followUser.getFullName() + " just followed you, let's check it out ")
                .createdBy(followUser)
                .build();
        notificationRepository.save(notification);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);

        ReceiverNotification receiverNotification = ReceiverNotification.builder()
                .receiverUser(targetUser)
                .notification(notification)
                .build();
        receiverNotificationRepository.save(receiverNotification);

        simpMessagingTemplate.convertAndSendToUser(targetUser.getEmail(), "/queue/notifications", notificationResponseDTO);
    }
}
