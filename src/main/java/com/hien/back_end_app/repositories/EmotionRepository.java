package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Emotion;
import com.hien.back_end_app.utils.enums.EmotionType;
import org.assertj.core.api.OptionalAssert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {

    @Query("""
            SELECT e FROM Emotion e
            INNER JOIN e.createdBy ec
            INNER JOIN e.post p
            WHERE ec.id=:createdId
            AND p.id=:postId
            """)
    Optional<Emotion> findByCreatedIdAndPostId(@Param("createdId") Long createdId, @Param("postId") Long postId);


    @Query("""
            SELECT e FROM Emotion e
            INNER JOIN e.createdBy ec
            INNER JOIN e.comment c
            WHERE ec.id=:createdId
            AND c.id=:commentId
            """)
    Optional<Emotion> findByCreatedIdAndCommentId(@Param("createdId") Long createdId, @Param("commentId") Long commentId);
}
