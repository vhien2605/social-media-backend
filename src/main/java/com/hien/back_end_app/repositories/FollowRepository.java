package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Follow;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @EntityGraph(attributePaths = {
            "followUser"
    })
    @Query("SELECT f FROM Follow f WHERE f.targetUser.id=:targetId")
    public List<Follow> getAllByTargetUserId(@Param("targetId") Long targetId);


    @Query("""
            SELECT f FROM Follow f
            INNER JOIN f.followUser u
            JOIN FETCH f.targetUser u1
            WHERE u.email=:followEmail
            """)
    public List<Follow> getAllTargetFollowers(@Param("followEmail") String followEmail);


    @Query("""
            DELETE FROM Follow f
            WHERE f.targetUser.id=:targetId
            AND f.followUser.id=:followId
            """)
    @Modifying
    @Transactional
    public void deleteByUserTargetIdAndUserFollowId(@Param("targetId") Long targetId
            , @Param("followId") Long followId);
}
