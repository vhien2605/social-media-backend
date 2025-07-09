package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.utils.enums.UserStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions"
    })
    public Optional<User> findByEmail(String email);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userStatus=:status WHERE u.email=:email")
    public void updateOnlineByEmail(@Param("email") String email, @Param("status") UserStatus status);
}
