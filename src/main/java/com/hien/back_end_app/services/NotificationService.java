package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.message.NotificationDetailResponseDTO;
import com.hien.back_end_app.dto.response.message.NotificationResponseDTO;
import com.hien.back_end_app.dto.response.post.UserRenderResponseDTO;
import com.hien.back_end_app.entities.Notification;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.NotificationMapper;
import com.hien.back_end_app.mappers.UserMapper;
import com.hien.back_end_app.repositories.NotificationRepository;
import com.hien.back_end_app.repositories.specification.SpecificationBuilder;
import com.hien.back_end_app.utils.commons.AppConst;
import com.hien.back_end_app.utils.commons.GlobalMethod;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.PostType;
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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;

    public PageResponseDTO<Object> getAllNotifications(Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findAllWithPagination(pageable);
        List<Long> notificationIds = notifications.stream().map(Notification::getId).toList();
        List<Notification> fetchedNotifications = notificationRepository.findWithReferences(notificationIds);
        Map<Long, Notification> idNotificationMap = fetchedNotifications
                .stream().collect(Collectors.toMap(Notification::getId, fn -> fn));
        List<NotificationResponseDTO> dtos = notificationIds.stream()
                .map(idNotificationMap::get)
                .map(notificationMapper::toDTO)
                .toList();
        return PageResponseDTO.builder()
                .pageSize(pageable.getPageSize())
                .pageNo(pageable.getPageNumber())
                .data(dtos)
                .totalPage(notifications.getTotalPages())
                .build();
    }

    public NotificationDetailResponseDTO seeDetail(Long notificationId) {
        Notification notification = notificationRepository.findWithId(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.SAVED_NOTIFICATION_NOT_EXIST));
        Set<UserRenderResponseDTO> dtos = notification.getReceivers().stream()
                .map(r -> userMapper.toDTO2(r.getReceiverUser())).collect(Collectors.toSet());
        NotificationDetailResponseDTO responseDTO = notificationMapper.toDetailDTO(notification);
        responseDTO.setReceivers(dtos);
        return responseDTO;
    }

    public PageResponseDTO<Object> getAllNotificationsFilter(Pageable pageable, String[] notification, String[] sortBy) {
        SpecificationBuilder<Notification> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);
        // loop other predicate
        for (String s : notification) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Notification> specification = builder.build();
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
        Page<Notification> notifications = notificationRepository.findAll(specification, sortedPageable);
        return PageResponseDTO.builder()
                .pageSize(pageable.getPageSize())
                .pageNo(pageable.getPageNumber())
                .data(fromPageResultToDTOResult(notifications))
                .totalPage(notifications.getTotalPages())
                .build();
    }

    public PageResponseDTO<Object> getMyNotifications(Pageable pageable) {
        String email = GlobalMethod.extractEmailFromContext();
        Page<Notification> notifications = notificationRepository.findByCreatedEmail(email, pageable);
        List<Long> notificationIds = notifications.stream().map(Notification::getId).toList();
        List<Notification> fetchedNotifications = notificationRepository.findWithReferences(notificationIds);
        Map<Long, Notification> idNotificationMap = fetchedNotifications
                .stream().collect(Collectors.toMap(Notification::getId, fn -> fn));
        List<NotificationResponseDTO> dtos = notificationIds.stream()
                .map(idNotificationMap::get)
                .map(notificationMapper::toDTO)
                .toList();
        return PageResponseDTO.builder()
                .pageSize(pageable.getPageSize())
                .pageNo(pageable.getPageNumber())
                .data(dtos)
                .totalPage(notifications.getTotalPages())
                .build();
    }

    public PageResponseDTO<Object> getMyNotificationsFilter(
            Pageable pageable,
            String[] notification,
            String[] sortBy
    ) {
        String email = GlobalMethod.extractEmailFromContext();
        SpecificationBuilder<Notification> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // loop other predicate
        builder.with(null, "emailNotificationReceiver", ":", email, null, null);
        for (String s : notification) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Notification> specification = builder.build();

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

        Page<Notification> notifications = notificationRepository.findAll(specification, sortedPageable);
        return PageResponseDTO.builder()
                .pageSize(pageable.getPageSize())
                .pageNo(pageable.getPageNumber())
                .data(fromPageResultToDTOResult(notifications))
                .totalPage(notifications.getTotalPages())
                .build();
    }

    private List<NotificationResponseDTO> fromPageResultToDTOResult(Page<Notification> notifications) {
        List<Long> notificationIds = notifications.stream().map(Notification::getId).toList();
        List<Notification> fetchedNotifications = notificationRepository.findWithReferences(notificationIds);
        Map<Long, Notification> idNotificationMap = fetchedNotifications
                .stream().collect(Collectors.toMap(Notification::getId, fn -> fn));
        List<NotificationResponseDTO> dtos = notificationIds.stream()
                .map(idNotificationMap::get)
                .map(notificationMapper::toDTO)
                .toList();
        return dtos;
    }
}
