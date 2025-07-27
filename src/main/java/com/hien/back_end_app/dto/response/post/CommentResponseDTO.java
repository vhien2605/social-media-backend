package com.hien.back_end_app.dto.response.post;


import com.hien.back_end_app.utils.enums.CommentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class CommentResponseDTO implements Serializable {
    private long id;
    private String content;
    private long createdBy;
    private CommentType type;
    private Set<EmotionResponseDTO> emotions;
}
