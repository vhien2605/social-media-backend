package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.message.NotificationResponseDTO;
import com.hien.back_end_app.entities.Notification;
import com.hien.back_end_app.mappers.NotificationMapper;
import com.hien.back_end_app.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public PageResponseDTO<Object> getAllNotifications(Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findAll(pageable);
        return null;
    }
}
