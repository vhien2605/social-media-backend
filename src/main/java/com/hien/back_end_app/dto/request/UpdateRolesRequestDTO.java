package com.hien.back_end_app.dto.request;

import com.hien.back_end_app.entities.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class UpdateRolesRequestDTO implements Serializable {
    @NotNull(message = "userId")
    private long userId;
    @NotEmpty(message = "roles are required")
    Set<RoleRequestDTO> roles;
}
