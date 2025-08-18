package com.hien.back_end_app.dto.response.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Builder
public class PostResponseDTO implements Serializable {
    private long id;
    private String content;
    private UserRenderResponseDTO createdBy;
    private Set<PostMediaResponseDTO> postMedias;
    private Set<EmotionResponseDTO> emotions;
    private Date createAt;
}
