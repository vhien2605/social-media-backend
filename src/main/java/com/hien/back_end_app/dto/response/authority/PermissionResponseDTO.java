package com.hien.back_end_app.dto.response.authority;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class PermissionResponseDTO implements Serializable {
    private String name;
    private String description;
}
