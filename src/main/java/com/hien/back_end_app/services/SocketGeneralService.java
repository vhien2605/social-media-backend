package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.GeneralNotificationRequestDTO;
import com.hien.back_end_app.dto.response.message.NotificationResponseDTO;
import com.hien.back_end_app.entities.Notification;
import com.hien.back_end_app.entities.ReceiverNotification;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.NotificationMapper;
import com.hien.back_end_app.repositories.NotificationRepository;
import com.hien.back_end_app.repositories.ReceiverNotificationRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketGeneralService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationMapper notificationMapper;
    private final ReceiverNotificationRepository receiverNotificationRepository;

    public void sendToUser(Long userId, GeneralNotificationRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        log.info("----------------------------------sending to specific user method ran---------------------------------");
        String content = dto.getContent();
        String createdEmail = accessor.getUser().getName();
        User createdUser = userRepository.findByEmailWithNoReferences(createdEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User receiverUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Notification notification = Notification.builder()
                .content(content)
                .createdBy(createdUser)
                .type(NotificationType.GENERAL)
                .build();
        notificationRepository.save(notification);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);

        ReceiverNotification receiverNotification = ReceiverNotification.builder()
                .receiverUser(receiverUser)
                .notification(notification)
                .build();
        receiverNotificationRepository.save(receiverNotification);

        simpMessagingTemplate.convertAndSendToUser(receiverUser.getEmail(), "/queue/notifications", notificationResponseDTO);
    }

    public void broadcast(GeneralNotificationRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        log.info("----------------------------------broadcast service method ran---------------------------------");
        String content = dto.getContent();
        String createdEmail = accessor.getUser().getName();
        User createdUser = userRepository.findByEmailWithNoReferences(createdEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Notification notification = Notification.builder()
                .content(content)
                .createdBy(createdUser)
                .type(NotificationType.GENERAL)
                .build();
        notificationRepository.save(notification);

        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);
        simpMessagingTemplate.convertAndSend("/topic/general", notificationResponseDTO);
    }
}
