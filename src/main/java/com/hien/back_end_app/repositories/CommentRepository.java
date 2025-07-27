package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Comment;
import com.hien.back_end_app.utils.enums.CommentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {
            "post",
            "createdBy",
            "post.group"
    })
    @Override
    Optional<Comment> findById(Long commentId);


    @Query("""
            SELECT c FROM Comment c
            INNER JOIN c.post p
            WHERE p.id=:postId
            AND c.type=:type
            ORDER BY c.createAt DESC
            """)
    Page<Comment> findCommentsByPostId(@Param("postId") Long postId, @Param("type") CommentType commentType, Pageable pageable);

    @Query("""
            SELECT DISTINCT c FROM Comment c
            LEFT JOIN FETCH c.emotions
            JOIN FETCH c.createdBy
            WHERE c.id IN :ids
            """)
    List<Comment> findCommentsWithEmotionsByIds(@Param("ids") List<Long> commentIds);

    @Query("""
            SELECT c FROM Comment c
            INNER JOIN c.replyTo cr
            WHERE cr.id=:id
            AND c.type=:type
            ORDER BY c.createAt DESC
            """)
    Page<Comment> findCommentsByReplyToId(@Param("id") Long replyToId, @Param("type") CommentType commentType, Pageable pageable);
}
