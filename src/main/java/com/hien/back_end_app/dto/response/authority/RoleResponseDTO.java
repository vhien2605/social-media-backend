package com.hien.back_end_app.dto.response.authority;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class RoleResponseDTO implements Serializable {
    private String name;
    private String description;
    private Set<PermissionResponseDTO> permissions;
}
