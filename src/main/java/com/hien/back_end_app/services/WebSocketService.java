package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.SocketMessageDTO;
import com.hien.back_end_app.dto.response.socket.MediaResponseDTO;
import com.hien.back_end_app.dto.response.socket.MessageResponseDTO;
import com.hien.back_end_app.dto.response.socket.NotificationResponseDTO;
import com.hien.back_end_app.entities.*;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.MessageMapper;
import com.hien.back_end_app.mappers.NotificationMapper;
import com.hien.back_end_app.repositories.*;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageMediaRepository messageMediaRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final MessageMapper messageMapper;

    @Transactional
    public void sendMessage(SocketMessageDTO request, Long conversationId, SimpMessageHeaderAccessor accessor) {
        if (request.getContent().isEmpty()) {
            return;
        }
        // check conversation
        Conversation conversation = conversationRepository.findById(conversationId).
                orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXIST));
        // check user have that conversation?
        String email = accessor.getUser().getName();
        User createdUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!(createdUser.getId() == conversation.getUser().getId())) {
            throw new AppException(ErrorCode.USER_NOT_HAVE_CONVERSATION);
        }


        if (request.getSocketMessageMediaDTO() == null) {
            Message message = Message.builder()
                    .content(request.getContent())
                    .sourceUser(createdUser)
                    .conversation(conversation)
                    .messageMedia(null)
                    .build();
            messageRepository.save(message);

            //send message
            MessageResponseDTO messageResponseDTO = messageMapper.toDTO(message);
            simpMessagingTemplate.convertAndSend("/topic/conversation/" + conversationId, messageResponseDTO);
        } else {
            //upload file to the cloud and get back url
            MultipartFile file = fileService.convertToMultipartFile(request.getSocketMessageMediaDTO());
            String fileUrl = fileService.uploadFile(file, request.getSocketMessageMediaDTO().getType(), "message_media");

            // save the message to database
            MessageMedia messageMedia = MessageMedia.builder()
                    .fileUrl(fileUrl)
                    .build();
            Message message = Message.builder()
                    .content(request.getContent())
                    .sourceUser(createdUser)
                    .conversation(conversation)
                    .messageMedia(messageMedia)
                    .build();
            messageMedia.setMessage(message);
            messageRepository.save(message);
            messageMediaRepository.save(messageMedia);

            //send message
            MessageResponseDTO messageResponseDTO = messageMapper.toDTO(message);
            simpMessagingTemplate.convertAndSend("/topic/conversation/" + conversationId, messageResponseDTO);
        }

        //create and send notification
        Notification notification = Notification.builder()
                .type(NotificationType.MESSAGE)
                .content(createdUser.getFullName() + " just sent you a message at conversation " + conversationId)
                .createdBy(createdUser)
                .conversation(conversation)
                .build();
        notificationRepository.save(notification);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);
        simpMessagingTemplate.convertAndSend("/topic/conversation/" + conversationId, notificationResponseDTO);
    }
}
