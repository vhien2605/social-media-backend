package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelegateCreatedRoleRequestDTO implements Serializable {
    @NotNull(message = "must fill this field")
    private long userId;

    @NotNull(message = "must fill this field")
    private long conversationId;
}
