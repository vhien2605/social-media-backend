package com.hien.back_end_app.dto.response.post;

import com.hien.back_end_app.entities.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class PostResponseDTO implements Serializable {
    private long id;
    private String content;
    private User createdBy;
    private Set<PostMediaResponseDTO> postMedias;
}
