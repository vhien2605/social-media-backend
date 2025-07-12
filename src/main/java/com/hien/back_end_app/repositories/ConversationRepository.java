package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c INNER JOIN c.participants u WHERE c.id=:conversationId AND u.id=:userId")
    public Optional<Conversation> findByConversationIdAndUserId(@Param("conversationId") long conversationId, @Param("userId") long userId);

    @EntityGraph(attributePaths = {
            "participants",
            "user.roles",
            "user.roles.permissions",
            "participants.roles",
            "participants.roles.permissions"
    })
    public Optional<Conversation> findById(Long id);
}
