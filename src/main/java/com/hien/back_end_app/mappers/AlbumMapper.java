package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.response.album.AlbumResponseDTO;
import com.hien.back_end_app.entities.Album;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AlbumImageMapper.class})
public interface AlbumMapper {
    AlbumResponseDTO toDTO(Album album);
}
