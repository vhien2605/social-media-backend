package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.SocketMessageDTO;
import com.hien.back_end_app.dto.response.socket.MediaResponseDTO;
import com.hien.back_end_app.dto.response.socket.MessageResponseDTO;
import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.Message;
import com.hien.back_end_app.entities.MessageMedia;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.repositories.ConversationRepository;
import com.hien.back_end_app.repositories.MessageMediaRepository;
import com.hien.back_end_app.repositories.MessageRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.utils.enums.ErrorCode;
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

    @Transactional
    public MessageResponseDTO sendMessage(SocketMessageDTO request, Long conversationId, SimpMessageHeaderAccessor accessor) {
        if (request.getContent().isEmpty()) {
            return null;
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
        MessageResponseDTO messageResponseDTO = MessageResponseDTO.builder()
                .id(message.getId())
                .content(message.getContent())
                .sourceId(message.getSourceUser().getId())
                .conversationId(message.getConversation().getId())
                .mediaResponseDTO(MediaResponseDTO.builder()
                        .fileUrl(fileUrl)
                        .build())
                .build();

        simpMessagingTemplate.convertAndSend("/topic/conversation/" + conversationId, messageResponseDTO);
        // send notification
        return messageResponseDTO;
    }
}
