package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.conversation.ConversationResponseDTO;
import com.hien.back_end_app.dto.response.socket.MessageResponseDTO;
import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.Message;
import com.hien.back_end_app.entities.MessageMedia;
import com.hien.back_end_app.mappers.MediaMessageMapper;
import com.hien.back_end_app.mappers.MessageMapper;
import com.hien.back_end_app.repositories.MessageMediaRepository;
import com.hien.back_end_app.repositories.MessageRepository;
import com.hien.back_end_app.repositories.specification.SpecificationBuilder;
import com.hien.back_end_app.utils.commons.AppConst;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        List<Message> fetchedMessages = messageRepository.findMessagesByConversationIdsWithFetch(messageIds);
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

    public PageResponseDTO<Object> getMessageByConversationIdWithFilter(Long conversationId
            , Pageable pageable
            , String[] message
            , String[] sortBy) {
        SpecificationBuilder<Message> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // loop conversation and build specification
        // add conversation id predicate
        builder.with(null, "conversation", ":", conversationId, null, null);
        // loop other predicate
        for (String s : message) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Message> specification = builder.build();


        // add sort by createAt desc
        List<Sort.Order> sortOrders = new ArrayList<>();
        sortOrders.add(new Sort.Order(Sort.Direction.DESC, "createAt"));
        for (String sb : sortBy) {
            Matcher sortMatcher = sortPattern.matcher(sb);
            if (sortMatcher.find()) {
                String field = sortMatcher.group(1);
                String value = sortMatcher.group(3);
                Sort.Direction direction = (value.equalsIgnoreCase("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sortOrders.add(new Sort.Order(direction, field));
            }
        }
        Sort sort = Sort.by(sortOrders);
        // insert sort property into pageable
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);

        Page<Message> messages = messageRepository.findAll(specification, sortedPageable);
        List<Long> messageIds = messages.stream().map(Message::getId).toList();
        // find reference
        List<Message> fetchedMessages = messageRepository.findMessagesByConversationIdsWithFetch(messageIds);
        Map<Long, Message> idMessageMap = fetchedMessages.stream().collect(Collectors.toMap(Message::getId, fm -> fm));
        // find media
        List<MessageMedia> messageMedias = messageMediaRepository.findAllById(messageIds);
        Map<Long, MessageMedia> idMessageMediaMap = messageMedias.stream().collect(Collectors.toMap(MessageMedia::getId, fm -> fm));

        List<MessageResponseDTO> dtos = messageIds.stream()
                .map(idMessageMap::get)
                .map(messageMapper::toDTO)
                .peek(dto -> dto.setMessageMedia(
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
