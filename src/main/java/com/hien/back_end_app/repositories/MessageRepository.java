package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    // no fetch
    @Query("""
            SELECT m FROM Message m
            INNER JOIN m.conversation c
            WHERE c.id=:conversationId
            ORDER BY m.createAt DESC
            """)
    Page<Message> findAllByConversationId(@Param("conversationId") Long conversationId, Pageable pageable);


    @Query("""
            SELECT m FROM Message m
            JOIN FETCH m.sourceUser
            JOIN FETCH m.conversation
            WHERE m.id IN :ids
            """)
    List<Message> findMessagesByConversationIdsWithFetch(@Param("ids") List<Long> ids);

    @Override
    Page<Message> findAll(Specification<Message> spec, Pageable pageable);
}
