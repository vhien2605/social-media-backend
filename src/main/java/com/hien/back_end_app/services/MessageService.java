package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.socket.MessageResponseDTO;
import com.hien.back_end_app.entities.Message;
import com.hien.back_end_app.entities.MessageMedia;
import com.hien.back_end_app.mappers.MediaMessageMapper;
import com.hien.back_end_app.mappers.MessageMapper;
import com.hien.back_end_app.repositories.MessageMediaRepository;
import com.hien.back_end_app.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final MessageMediaRepository messageMediaRepository;
    private final MediaMessageMapper mediaMessageMapper;

    public PageResponseDTO<Object> getMessageWithPagination(Long conversationId, Pageable pageable) {
        // find pageable
        Page<Message> messages = messageRepository.findAllByConversationId(conversationId, pageable);
        List<Long> messageIds = messages.stream().map(Message::getId).toList();
        //fetch
        List<Message> fetchedMessages = messageRepository.findConversationsByIdsWithFetch(messageIds);
        Map<Long, Message> idMessageMap = fetchedMessages.stream().collect(Collectors.toMap(Message::getId, m -> m));
        // fetch media because mapIds
        List<MessageMedia> messageMedias = messageMediaRepository.findAllById(messageIds);
        Map<Long, MessageMedia> idMessageMediaMap = messageMedias.stream()
                .collect(Collectors.toMap(MessageMedia::getId, md -> md));

        List<MessageResponseDTO> dtos = messageIds.stream()
                .map(idMessageMap::get)
                .map(messageMapper::toDTO)
                .peek(dto ->
                        dto.setMessageMedia(
                                mediaMessageMapper.toDTO(idMessageMediaMap.get(dto.getId()))))
                .toList();

        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(messages.getTotalPages())
                .data(dtos)
                .build();
    }
}
