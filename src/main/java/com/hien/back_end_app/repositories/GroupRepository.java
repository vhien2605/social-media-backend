package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @EntityGraph(attributePaths = {
            "createdBy"
    })
    public Optional<Group> findById(Long groupId);

    @Query("""
            SELECT g FROM Group g
            JOIN FETCH g.groupUsers gu
            JOIN FETCH g.createdBy
            JOIN FETCH gu.user
            WHERE g.id=:groupId
            """)
    public Optional<Group> findByIdWithGroupUsers(@Param("groupId") Long groupId);
}
