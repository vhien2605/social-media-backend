package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.UploadPostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadPostRequestRepository extends JpaRepository<UploadPostRequest, Long> {
    @Query("""
            SELECT DISTINCT ulr FROM UploadPostRequest ulr
            JOIN FETCH ulr.createdBy
            JOIN FETCH ulr.group g
            JOIN FETCH g.createdBy
            LEFT JOIN FETCH ulr.medias
            WHERE ulr.id=:id
            """)
    Optional<UploadPostRequest> findByIdWithReferences(@Param("id") Long aLong);

    @Query("""
            SELECT up FROM UploadPostRequest up
            INNER JOIN up.group g
            WHERE g.id=:groupId
            ORDER BY up.createAt DESC
            """)
    Page<UploadPostRequest> findUploadsByGroupId(@Param("groupId") Long groupId, Pageable pageable);


    @Query("""
            SELECT up FROM UploadPostRequest up
            LEFT JOIN FETCH up.medias
            JOIN FETCH up.createdBy
            WHERE up.id IN :ids
            """)
    List<UploadPostRequest> findUploadsWithMediasByIds(@Param("ids") List<Long> ids);
}
