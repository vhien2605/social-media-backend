package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.SocketMessageDTO;
import com.hien.back_end_app.dto.response.message.MessageResponseDTO;
import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.Message;
import com.hien.back_end_app.entities.Notification;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.repositories.ConversationRepository;
import com.hien.back_end_app.repositories.MessageRepository;
import com.hien.back_end_app.repositories.NotificationRepository;
import com.hien.back_end_app.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SocketMessageServiceTest {
    @Autowired
    private SocketConversationService socketConversationService;
    @MockitoBean
    private ConversationRepository conversationRepository;
    @MockitoBean
    private MessageRepository messageRepository;

    @MockitoBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("send message test")
    public void sendMessageTest() {
        SocketMessageDTO socketMessageDTO = SocketMessageDTO.builder()
                .content("test")
                .build();
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setUser(new TestingAuthenticationToken("hien", null));
        User user1 = User.builder()
                .fullName("hien")
                .email("hien@gmail.com")
                .build();
        User user2 = User.builder()
                .fullName("minh")
                .email("minh@gmail.com")
                .build();
        Mockito.when(conversationRepository.findByIdWithUserCreatedAndParticipants(Mockito.any(Long.class)))
                .thenReturn(
                        Optional.of(Conversation.builder()
                                .id(1L)
                                .name("sample")
                                .isGroup(false)
                                .user(user1)
                                .participants(new HashSet<>(Set.of(user2)))
                                .build())
                );
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class)))
                .thenReturn(Optional.of(User.builder()
                        .fullName("hien")
                        .email("hien@gmail.com")
                        .build()));
        Mockito.when(messageRepository.save(Mockito.any(Message.class)))
                .thenReturn(Message.builder()
                        .content("test")
                        .conversation(Conversation.builder()
                                .id(1)
                                .name("sample")
                                .isGroup(false)
                                .user(user1)
                                .participants(Set.of(user2))
                                .build())
                        .build());
        Mockito.when(notificationRepository.save(Mockito.any(Notification.class)))
                .thenReturn(Notification.builder()
                        .content("aasdsd")
                        .build());
        socketConversationService.sendMessage(socketMessageDTO, 1L, accessor);

        // check that message was sent or not
        Mockito.verify(simpMessagingTemplate, Mockito.times(1))
                .convertAndSend(
                        Mockito.eq("/topic/conversation/1"),
                        Mockito.any(MessageResponseDTO.class)
                );
    }
}
