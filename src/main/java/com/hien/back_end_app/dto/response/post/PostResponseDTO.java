package com.hien.back_end_app.dto.response.post;

import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import com.hien.back_end_app.entities.User;
import com.mysql.cj.conf.PropertyDefinitions;
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
    private UserRenderPostResponseDTO createdBy;
    private Set<PostMediaResponseDTO> postMedias;
    private Set<EmotionResponseDTO> emotions;
    private Date createAt;
}
