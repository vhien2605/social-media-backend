package com.hien.back_end_app.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketAddMemberRequestDTO {
    @NotEmpty(message = "adding users must not be empty")
    private Set<Long> ids;
}
