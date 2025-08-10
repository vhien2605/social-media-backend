package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralNotificationRequestDTO implements Serializable {
    @NotBlank(message = "Content must not be blank")
    private String content;
}
