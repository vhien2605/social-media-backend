package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM User u INNER JOIN u.roles r WHERE u.id=:id")
    public List<Role> findAllRoleByUserId(@Param("id") Long userId);


    @EntityGraph(attributePaths = {
            "permissions"
    })
    public Optional<Role> findByName(String name);
}
