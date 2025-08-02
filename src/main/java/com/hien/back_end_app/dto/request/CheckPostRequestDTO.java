package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckPostRequestDTO implements Serializable {
    @NotNull(message = "accepted not be null")
    private boolean accepted;
}
