package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.AlbumPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumImageRepository extends JpaRepository<AlbumPhoto, Long> {
}
