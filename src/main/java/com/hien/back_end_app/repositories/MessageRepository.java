package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
