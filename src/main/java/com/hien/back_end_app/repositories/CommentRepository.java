package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    @EntityGraph(attributePaths = {
            "post",
            "createdBy"
    })
    @Override
    Optional<Comment> findById(Long commentId);
}
