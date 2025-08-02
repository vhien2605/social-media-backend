package com.hien.back_end_app.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRolesRequestDTO implements Serializable {
    @NotNull(message = "userId")
    private long userId;
    @NotEmpty(message = "roles are required")
    Set<RoleRequestDTO> roles;
}
