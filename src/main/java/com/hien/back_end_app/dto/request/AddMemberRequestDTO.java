package com.hien.back_end_app.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberRequestDTO implements Serializable {
    @NotNull(message = "not be null")
    private long groupId;
    @NotNull(message = "not be null")
    private long addedUserId;
}
