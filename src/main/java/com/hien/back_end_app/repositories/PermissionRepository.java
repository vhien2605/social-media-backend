package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("""
            SELECT p FROM Permission p
            WHERE p.name IN :names
            """)
    public List<Permission> findAllInNames(@Param("names") List<String> names);
}
