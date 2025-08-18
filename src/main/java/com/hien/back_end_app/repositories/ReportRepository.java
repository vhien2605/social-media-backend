package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    @Query("""
            SELECT r FROM Report r
            JOIN FETCH r.createdUser
            LEFT JOIN FETCH r.post
            LEFT JOIN FETCH r.group
            LEFT JOIN FETCH r.comment
            ORDER BY r.createAt DESC
            """)
    Page<Report> getAllReportsWithReferences(Pageable pageable);


    @Query("""
            SELECT r FROM Report r
            JOIN FETCH r.createdUser
            LEFT JOIN FETCH r.post
            LEFT JOIN FETCH r.group
            LEFT JOIN FETCH r.comment
            WHERE
            (
                r.reportType = com.hien.back_end_app.utils.enums.ReportType.GROUP_POST_REPORT
                OR
                r.reportType = com.hien.back_end_app.utils.enums.ReportType.GROUP_COMMENT_REPORT
            )
            AND
            (
                r.group.id=:groupId
            )
            """)
    Page<Report> getAllGroupReportsWithReferences(@Param("groupId") Long groupId, Pageable pageable);
}
