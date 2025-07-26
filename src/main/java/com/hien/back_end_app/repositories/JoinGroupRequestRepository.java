package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.JoinGroupRequest;
import com.hien.back_end_app.utils.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface JoinGroupRequestRepository extends JpaRepository<JoinGroupRequest, Long> {
    @EntityGraph(attributePaths = {
            "group",
            "group.createdBy",
            "createdBy"
    })
    @Override
    Optional<JoinGroupRequest> findById(Long aLong);

    @Query("""
            SELECT j FROM JoinGroupRequest j
            INNER JOIN j.group g
            JOIN FETCH j.createdBy
            WHERE g.id=:id
            ORDER BY j.createAt DESC
            """)
    Page<JoinGroupRequest> findByGroupId(@Param("id") Long groupId, Pageable pageable);
}
