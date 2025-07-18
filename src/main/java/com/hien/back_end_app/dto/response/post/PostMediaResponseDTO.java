package com.hien.back_end_app.dto.response.post;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PostMediaResponseDTO {
    private String fileUrl;
}
