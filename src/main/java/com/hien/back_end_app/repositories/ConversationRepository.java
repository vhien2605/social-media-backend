package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c INNER JOIN c.participants u WHERE c.id=:conversationId AND u.id=:userId")
    public Optional<Conversation> findByConversationIdAndUserId(@Param("conversationId") long conversationId, @Param("userId") long userId);

    @EntityGraph(attributePaths = {
            "user",
            "participants",
            "user.roles",
            "user.roles.permissions",
            "participants.roles",
            "participants.roles.permissions"
    })
    public Optional<Conversation> findById(Long id);

    @EntityGraph(attributePaths = {
            "user",
            "participants"
    })
    @Query("SELECT c FROM Conversation c WHERE c.id=:id")
    public Optional<Conversation> findByIdWithUserCreatedAndParticipants(@Param("id") Long id);

    @Override
    public void deleteById(Long aLong);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM participant_conversation p WHERE p.user_id IN :ids", nativeQuery = true)
    public void deleteJoinRecords(@Param("ids") List<Long> ids);
}
