package com.hien.back_end_app.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinGroupRequestDTO implements Serializable {
    private long groupId;
    @NotBlank(message = "message must not be blank")
    private String message;
}
