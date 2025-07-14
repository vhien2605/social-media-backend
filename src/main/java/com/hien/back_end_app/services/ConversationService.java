package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.conversation.ConversationResponseDTO;
import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.mappers.ConversationMapper;
import com.hien.back_end_app.repositories.ConversationRepository;
import com.hien.back_end_app.utils.commons.GlobalMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;


    public PageResponseDTO<Object> getAllConversations(Pageable pageable) {
        Page<Conversation> conversations = conversationRepository.findAll(pageable);
        List<Long> conversationIds = conversations.stream().map(Conversation::getId).toList();
        List<Conversation> fetchedConversations = conversationRepository.findAllWithIdsAndReferences(conversationIds);
        List<ConversationResponseDTO> dtos = fetchedConversations.stream().map(conversationMapper::toDTO).toList();
        return PageResponseDTO.builder()
                .data(dtos)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(conversations.getTotalPages())
                .build();
    }

    public PageResponseDTO<Object> getMyConversations(Pageable pageable) {
        String email = GlobalMethod.extractEmailFromContext();
        Page<Conversation> conversations = conversationRepository.findAllByUserEmail(email, pageable);
        List<Long> conversationIds = conversations.stream().map(Conversation::getId).toList();
        List<ConversationResponseDTO> dtos = conversationRepository.findAllWithIdsAndReferences(conversationIds)
                .stream().map(conversationMapper::toDTO).toList();
        return PageResponseDTO.builder()
                .data(dtos)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(conversations.getTotalPages())
                .build();
    }
}
