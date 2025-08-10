package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("""
            SELECT n FROM Notification n
            ORDER BY n.createAt DESC
            """)
    public List<Notification> findAllWithPagination(Pageable pageable);


    @Query("""
            SELECT n FROM Notification n
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
}
