package com.hien.back_end_app.dto.response.group;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class PostRequestMediaResponseDTO implements Serializable {
    private String fileUrl;
}
