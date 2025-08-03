package com.hien.back_end_app.dto.request;


import com.hien.back_end_app.utils.enums.EmotionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionRequestDTO implements Serializable {
    @NotNull(message = "emotion not be null")
    private EmotionType type;
}
