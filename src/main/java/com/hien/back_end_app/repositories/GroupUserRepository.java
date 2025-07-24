package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    @Query("""
            SELECT gu FROM GroupUser gu
            INNER JOIN gu.user guu
            INNER JOIN gu.group gug
            WHERE guu.id=:userId
            AND gug.id=:groupId
            """)
    public Optional<GroupUser> findByGroupIdAndUserId(@Param("groupId") Long groupId
            , @Param("userId") Long userId
    );
}
