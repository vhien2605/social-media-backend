package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.utils.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.albums a
            WHERE u.email=:email
            """)
    public Optional<User> findByEmailWithAlbums(@Param("email") String email);

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

    @Query("""
            SELECT u FROM User u
            INNER JOIN FETCH u.roles r
            INNER JOIN FETCH r.permissions
            LEFT JOIN FETCH u.follows f
            LEFT JOIN FETCH f.followUser
            LEFT JOIN FETCH u.albums al
            LEFT JOIN FETCH al.albumPhotos
            WHERE u.id=:id
            """)
    Optional<User> findByIdWithFollowersAndAlbums(@Param("id") Long id);


    @Query("""
            SELECT u FROM User u
            JOIN FETCH u.roles r
            JOIN FETCH r.permissions
            WHERE u.id=:id
            """)
    Optional<User> findByIdWithRoles(@Param("id") Long userId);

    public boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("""
            UPDATE User u SET u.password=:password
            WHERE u.email=:email
            """)
    public void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);


    @Override
    Page<User> findAll(Pageable pageable);
}
