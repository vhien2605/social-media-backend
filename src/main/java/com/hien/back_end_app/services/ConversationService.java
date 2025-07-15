package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.conversation.ConversationResponseDTO;
import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.mappers.ConversationMapper;
import com.hien.back_end_app.repositories.ConversationRepository;
import com.hien.back_end_app.repositories.specification.SpecificationBuilder;
import com.hien.back_end_app.utils.commons.AppConst;
import com.hien.back_end_app.utils.commons.GlobalMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;


    public PageResponseDTO<Object> getAllConversations(Pageable pageable) {
        Page<Conversation> conversations = conversationRepository.findAll(pageable);
        List<Long> conversationIds = conversations.stream().map(Conversation::getId).toList();
        List<Conversation> fetchedConversations = conversationRepository.findAllWithIdsAndCreatedUserReferences(conversationIds);
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
        List<ConversationResponseDTO> dtos = conversationRepository.findAllWithIdsAndCreatedUserReferences(conversationIds)
                .stream().map(conversationMapper::toDTO).toList();
        return PageResponseDTO.builder()
                .data(dtos)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(conversations.getTotalPages())
                .build();
    }

    public PageResponseDTO<Object> getMyConversationsWithAdvancedFilter(Pageable pageable, String[] conversation, String[] sortBy) {
        log.info("------------------------------------get my conversation advanced filter service---------------------------------------");
        String email = GlobalMethod.extractEmailFromContext();
        // build specification
        SpecificationBuilder<Conversation> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // add condition email to builder
        builder.with(null, "email", ":", email, null, null);

        for (String s : conversation) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Conversation> specification = builder.build();

        // insert sort property into pageable
        List<Sort.Order> sortOrders = new ArrayList<>();
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
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);

        //call specification jpa method with no fetch
        Page<Conversation> conversations = conversationRepository.findAll(specification, sortedPageable);
        List<Long> conversationIds = conversations.stream().map(Conversation::getId).toList();
        List<Conversation> fetchedConversations = conversationRepository.findAllWithIdsAndCreatedUserReferences(conversationIds);
        Map<Long, Conversation> idConversationMap = fetchedConversations.stream()
                .collect(Collectors.toMap(Conversation::getId, c -> c));
        List<ConversationResponseDTO> dtos = conversationIds.stream()
                .map(idConversationMap::get)
                .map(conversationMapper::toDTO)
                .toList();
        return PageResponseDTO.builder()
                .data(dtos)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(conversations.getTotalPages())
                .build();
    }

    public PageResponseDTO<Object> getAllConversationsWithAdvancedFilter(Pageable pageable, String[] conversation, String[] sortBy) {
        SpecificationBuilder<Conversation> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // loop conversation and build specification
        for (String s : conversation) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Conversation> specification = builder.build();

        // insert sort property into pageable
        List<Sort.Order> sortOrders = new ArrayList<>();
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
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);

        Page<Conversation> conversations = conversationRepository.findAll(specification, pageable);
        List<Long> conversationIds = conversations.stream().map(Conversation::getId).toList();

        // fetched conversation
        List<Conversation> fetchedConversations = conversationRepository.findAllWithIdsAndCreatedUserReferences(conversationIds);
        // create map to store sort order in pagination
        Map<Long, Conversation> idConversationMap = fetchedConversations
                .stream()
                .collect(Collectors.toMap(Conversation::getId, c -> c));

        List<ConversationResponseDTO> dtos = conversationIds.stream()
                .map(idConversationMap::get)
                .map(conversationMapper::toDTO)
                .toList();
        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(conversations.getTotalPages())
                .data(dtos)
                .build();
    }
}
