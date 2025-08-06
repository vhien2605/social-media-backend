package com.hien.back_end_app.dto.response.authority;


import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponseDTO implements Serializable {
    private String name;
    private String description;
}
