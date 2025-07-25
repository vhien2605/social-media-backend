package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Post;
import com.hien.back_end_app.utils.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @EntityGraph(attributePaths = {
            "createdBy",
            "group"
    })
    @Override
    Optional<Post> findById(Long postId);

    @Query("""
            SELECT p FROM Post p
            INNER JOIN p.createdBy pc
            WHERE pc.id IN :targetIds
            AND p.type=:type
            ORDER BY p.createAt DESC
            """)
    Page<Post> findFollowPosts(@Param("targetIds") List<Long> ids, @Param("type") PostType type, Pageable pageable);


    @Query("""
            SELECT DISTINCT p FROM Post p
            LEFT JOIN FETCH p.postMedias
            LEFT JOIN FETCH p.emotions
            WHERE p.id IN :postIds
            """)
    List<Post> findPostsWithPostMediaAndEmotions(@Param("postIds") List<Long> postIds);


    @Override
    public Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    @Query("""
            SELECT p FROM Post p
            INNER JOIN p.createdBy pc
            WHERE pc.email=:email
            AND p.type=:type
            ORDER BY p.createAt DESC
            """)
    public Page<Post> findMyPosts(@Param("email") String email, @Param("type") PostType type, Pageable pageable);
}
