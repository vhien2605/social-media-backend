package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {


    @EntityGraph(attributePaths = {
            "createdBy"
    })
    public Optional<Group> findById(Long groupId);
}
