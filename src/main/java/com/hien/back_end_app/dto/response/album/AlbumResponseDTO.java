package com.hien.back_end_app.dto.response.album;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class AlbumResponseDTO implements Serializable {
    private long id;
    private String title;
    private String description;
    private Set<AlbumImageResponseDTO> albumPhotos;
}
