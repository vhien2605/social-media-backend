package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    @Query("""
            SELECT n FROM Notification n
            ORDER BY n.createAt DESC
            """)
    public Page<Notification> findAllWithPagination(Pageable pageable);

    @Query("""
            SELECT n FROM Notification n
            INNER JOIN n.receivers r
            INNER JOIN r.receiverUser ru
            WHERE ru.email=:email
            ORDER BY n.createAt DESC
            """)
    public Page<Notification> findByCreatedEmail(@Param("email") String email, Pageable pageable);


    @Query("""
            SELECT DISTINCT n FROM Notification n
            JOIN FETCH n.createdBy
            LEFT JOIN FETCH n.post
            LEFT JOIN FETCH n.comment
            LEFT JOIN FETCH n.emotion
            LEFT JOIN FETCH n.group
            LEFT JOIN FETCH n.conversation
            WHERE n.id IN :ids
            ORDER BY n.createAt DESC
            """)
    public List<Notification> findWithReferences(@Param("ids") List<Long> ids);

    @Query("""
            SELECT DISTINCT n FROM Notification n
            JOIN FETCH n.createdBy
            LEFT JOIN FETCH n.post
            LEFT JOIN FETCH n.comment
            LEFT JOIN FETCH n.emotion
            LEFT JOIN FETCH n.group
            LEFT JOIN FETCH n.conversation
            LEFT JOIN FETCH n.receivers r
            LEFT JOIN FETCH r.receiverUser
            WHERE n.id=:id
            """)
    public Optional<Notification> findWithId(@Param("id") Long id);


    @Override
    Page<Notification> findAll(Specification<Notification> spec, Pageable pageable);
}
