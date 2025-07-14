package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long>, JpaSpecificationExecutor<Conversation> {
    @Query("SELECT c FROM Conversation c INNER JOIN c.participants u WHERE c.id=:conversationId AND u.id=:userId")
    public Optional<Conversation> findByConversationIdAndUserId(@Param("conversationId") long conversationId, @Param("userId") long userId);

    @EntityGraph(attributePaths = {
            "user",
            "user.roles",
            "user.roles.permissions",
            "participants",
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


    @Override
    Page<Conversation> findAll(Pageable pageable);
    
    //get with no fetch
    @Query("SELECT c FROM Conversation c INNER JOIN c.user u WHERE u.email=:email")
    Page<Conversation> findAllByUserEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT c FROM Conversation c INNER JOIN c.user u WHERE u.email=:email")
    List<Conversation> findAllWithUserEmailNoPagination(@Param("email") String email);


    @Override
    Page<Conversation> findAll(Specification<Conversation> spec, Pageable pageable);


    @Query("""
            SELECT DISTINCT c FROM Conversation c
            LEFT JOIN FETCH c.user u
            LEFT JOIN FETCH u.roles ur
            LEFT JOIN FETCH ur.permissions
            WHERE c.id IN :ids
            """
    )
    List<Conversation> findAllWithIdsAndCreatedUserReferences(@Param("ids") List<Long> ids);
}
