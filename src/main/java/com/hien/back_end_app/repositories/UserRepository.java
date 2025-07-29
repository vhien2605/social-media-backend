package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.utils.enums.UserStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions"
    })
    public Optional<User> findByEmail(String email);

    @Query("""
            SELECT u FROM User u
            WHERE u.email=:email
            """)
    public Optional<User> findByEmailWithNoReferences(@Param("email") String email);

    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions"
    })
    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    public List<User> findAllByIds(@Param("ids") List<Long> ids);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userStatus=:status WHERE u.email=:email")
    public void updateOnlineByEmail(@Param("email") String email, @Param("status") UserStatus status);


    @Override
    Optional<User> findById(Long aLong);
    
    public boolean existsByEmail(String email);
}
