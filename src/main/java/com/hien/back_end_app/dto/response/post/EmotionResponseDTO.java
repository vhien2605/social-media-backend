package com.hien.back_end_app.dto.response.post;


import com.hien.back_end_app.utils.enums.EmotionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class EmotionResponseDTO {
    private long id;
    private EmotionType type;
}
