package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.UploadPostRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadPostRequestRepository extends JpaRepository<UploadPostRequest, Long> {
    @Query("""
            SELECT ulr FROM UploadPostRequest ulr
            JOIN FETCH ulr.createdBy
            JOIN FETCH ulr.group g
            JOIN FETCH g.createdBy
            LEFT JOIN FETCH ulr.medias
            WHERE ulr.id=:id
            """)
    Optional<UploadPostRequest> findByIdWithReferences(@Param("id") Long aLong);
}
