package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.ReceiverNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverNotificationRepository extends JpaRepository<ReceiverNotification, Long> {
}
