package com.hien.back_end_app.repositories;

import com.hien.back_end_app.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
}
