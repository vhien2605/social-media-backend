package com.hien.back_end_app.dto.response.album;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class AlbumImageResponseDTO implements Serializable {
    private String imageUrl;
}
