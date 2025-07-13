package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.CreateConversationRequestDTO;
import com.hien.back_end_app.dto.request.SocketAddMemberRequestDTO;
import com.hien.back_end_app.dto.request.SocketDeleteMemberRequestDTO;
import com.hien.back_end_app.dto.request.SocketMessageDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketConversationService {
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
            String fileUrl = fileService.uploadFile(file,
                    request.getSocketMessageMediaDTO().getType(), "message_media");

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

        //create notification
        Notification notification = Notification.builder()
                .type(NotificationType.MESSAGE)
                .content(createdUser.getFullName() + " just sent you a message at conversation " + conversationId)
                .createdBy(createdUser)
                .conversation(conversation)
                .build();
        notificationRepository.save(notification);

        // send notification
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);
        Set<User> participants = conversation.getParticipants();
        for (User u : participants) {
            simpMessagingTemplate.convertAndSendToUser(u.getEmail(), "/queue/notifications", notificationResponseDTO);
        }
    }


    @Transactional
    public void addMemberToConversation(SocketAddMemberRequestDTO dto, Long conversationId, SimpMessageHeaderAccessor accessor) {
        String email = accessor.getUser().getName();
        Set<Long> userIds = dto.getIds();
        Conversation conversation = conversationRepository.findByIdWithUserCreatedAndParticipants(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXIST));
        if (!conversation.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        if (!conversation.isGroup()) {
            throw new AppException(ErrorCode.CONVERSATION_SIZE_INVALID);
        }
        Set<User> participants = conversation.getParticipants();
        // filter that user are already in the conversation will be removed from new adder
        List<User> users = userRepository.findAllByIds(new ArrayList<>(userIds))
                .stream().filter(u -> participants.stream().noneMatch(p -> p.getId() == u.getId())).toList();

        if (users.isEmpty()) {
            return;
        }

        // concat set to add new members in conversation
        participants.addAll(users);
        conversation.setParticipants(participants);
        conversationRepository.save(conversation);

        StringBuilder newUsersContent = new StringBuilder();
        for (User u : users) {
            newUsersContent.append(u.getFullName()).append(" ");
        }

        Message message = Message.builder()
                .messageMedia(null)
                .content(conversation.getUser().getFullName() + " just added new members: " + newUsersContent + " to the conversation")
                .sourceUser(conversation.getUser())
                .conversation(conversation)
                .build();
        messageRepository.save(message);
        MessageResponseDTO messageResponseDTO = messageMapper.toDTO(message);
        // send message
        simpMessagingTemplate.convertAndSend("/topic/conversation/" + conversationId, messageResponseDTO);
    }

    public void deleteMemberFromConversation(SocketDeleteMemberRequestDTO dto, Long conversationId, SimpMessageHeaderAccessor accessor) {
        Set<Long> ids = dto.getIds();
        String email = accessor.getUser().getName();
        Conversation conversation = conversationRepository.findByIdWithUserCreatedAndParticipants(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXIST));
        User createdUser = conversation.getUser();
        if (!createdUser.getEmail().equals(email)) {
            log.error("---------------access denied------------------");
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Set<User> participants = conversation.getParticipants();

        if (ids.size() > participants.size()) {
            log.error("---------------input size is greater than participants size------------------");
            throw new AppException(ErrorCode.CONVERSATION_SIZE_INVALID);
        }

        if (ids.size() == participants.size()) {
            // when delele all the participants->delete conversation and jointable
            conversationRepository.deleteJoinRecords(new ArrayList<>(ids));
            conversationRepository.deleteById(conversationId);
            return;
        }
        // delete ids in jointable and send message
        conversationRepository.deleteJoinRecords(new ArrayList<>(ids));

        //create message
        StringBuilder newUsersContent = new StringBuilder();
        for (Long u : ids) {
            newUsersContent.append(u).append(" ");
        }
        Message message = Message.builder()
                .messageMedia(null)
                .content(conversation.getUser().getFullName() + " just deleted members: " + newUsersContent + " from the conversation")
                .sourceUser(conversation.getUser())
                .conversation(conversation)
                .build();
        messageRepository.save(message);
        MessageResponseDTO messageResponseDTO = messageMapper.toDTO(message);
        //send stomp message
        simpMessagingTemplate.convertAndSend("/topic/conversation/" + conversationId, messageResponseDTO);
    }

    @Transactional
    public void createConversation(CreateConversationRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        String name = dto.getName();
        Set<Long> participantIds = dto.getParticipants();
        String email = accessor.getUser().getName();

        User createdUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<User> participants = userRepository.findAllByIds(new ArrayList<>(participantIds));

        Conversation conversation = Conversation.builder()
                .name(name)
                .user(createdUser)
                .isGroup(false)
                .participants(new HashSet<>(participants))
                .build();

        // save conversation
        conversationRepository.save(conversation);

        //save and send message to conversation
        Message message = Message.builder()
                .conversation(conversation)
                .sourceUser(createdUser)
                .content(createdUser.getFullName() + " just create conversation ")
                .build();
        messageRepository.save(message);
        MessageResponseDTO messageResponseDTO = messageMapper.toDTO(message);
        simpMessagingTemplate.convertAndSend("/topic/conversation/" + conversation.getId(), messageResponseDTO);

        // save and send notifications to participants
        Notification notification = Notification.builder()
                .conversation(conversation)
                .createdBy(createdUser)
                .content(createdUser.getFullName() + " just create conversation with you, let's check it out")
                .type(NotificationType.MESSAGE)
                .build();
        notificationRepository.save(notification);
        NotificationResponseDTO notificationResponseDTO = notificationMapper.toDTO(notification);
        for (User u : participants) {
            simpMessagingTemplate.convertAndSendToUser(u.getEmail(), "/queue/notifications", notificationResponseDTO);
        }
    }

    @Transactional
    public void changeToGroup(Long conversationId, SimpMessageHeaderAccessor accessor) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXIST));
        User createdUser = conversation.getUser();
        String email = accessor.getUser().getName();
        if (!createdUser.getEmail().equals(email)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        conversation.setGroup(true);
        conversationRepository.save(conversation);
        
        //save and send message to conversation
        Message message = Message.builder()
                .conversation(conversation)
                .sourceUser(createdUser)
                .content(createdUser.getFullName() + " just change conversation to group chat")
                .build();
        messageRepository.save(message);
        MessageResponseDTO messageResponseDTO = messageMapper.toDTO(message);
        simpMessagingTemplate.convertAndSend("/topic/conversation/" + conversation.getId(), messageResponseDTO);
    }
}
