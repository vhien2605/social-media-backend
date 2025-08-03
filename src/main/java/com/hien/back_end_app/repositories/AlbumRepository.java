package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("""
            SELECT a FROM Album a
            INNER JOIN a.user u
            WHERE u.id=:userId
            ORDER BY a.createAt DESC
            """)
    Page<Album> findAllByUserId(@Param("userId") Long userId, Pageable pageable);


    @Query("""
            SELECT a FROM Album a
            LEFT JOIN FETCH a.albumPhotos
            WHERE a.id IN :ids
            """)
    List<Album> findByIdsWithReferences(@Param("ids") List<Long> ids);
}
