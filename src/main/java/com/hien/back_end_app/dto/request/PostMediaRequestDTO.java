package com.hien.back_end_app.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PostMediaRequestDTO {
    private String name;
    private String type;
    private String base64Data;
}
