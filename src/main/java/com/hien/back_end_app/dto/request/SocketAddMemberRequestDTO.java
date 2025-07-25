package com.hien.back_end_app.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class SocketAddMemberRequestDTO {
    @NotEmpty(message = "adding users must not be empty")
    private Set<Long> ids;
}
