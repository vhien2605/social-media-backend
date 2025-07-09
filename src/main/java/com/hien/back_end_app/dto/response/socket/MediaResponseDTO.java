package com.hien.back_end_app.dto.response.socket;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MediaResponseDTO {
    private String fileUrl;
}
