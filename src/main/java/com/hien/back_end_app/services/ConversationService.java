package com.hien.back_end_app.services;

import com.hien.back_end_app.dto.request.CreateConversationRequestDTO;
import com.hien.back_end_app.dto.response.conversation.ConversationResponseDTO;
import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.ConversationMapper;
import com.hien.back_end_app.repositories.ConversationRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.utils.commons.GlobalMethod;
import com.hien.back_end_app.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationMapper conversationMapper;

    public ConversationResponseDTO create(CreateConversationRequestDTO dto) {
        if (dto.getParticipants().size() > 1) {
            throw new AppException(ErrorCode.CONVERSATION_SIZE_INVALID);
        }
        String name = dto.getName();
        String email = GlobalMethod.extractEmailFromContext();
        Set<Long> participantIds = dto.getParticipants();
        User createdUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<User> participants = userRepository.findAllByIds(new ArrayList<>(participantIds));
        Conversation conversation = Conversation.builder()
                .user(createdUser)
                .participants(new HashSet<>(participants))
                .name(name)
                .build();
        return conversationMapper.toDTO(conversationRepository.save(conversation));
    }

    public ConversationResponseDTO toGroup(long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXIST));
        conversation.setGroup(true);
        return conversationMapper.toDTO(conversationRepository.save(conversation));
    }


}
