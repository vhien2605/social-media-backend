package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.response.album.AlbumImageResponseDTO;
import com.hien.back_end_app.entities.AlbumPhoto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumImageMapper {
    AlbumImageResponseDTO toDTO(AlbumPhoto albumPhoto);
}
