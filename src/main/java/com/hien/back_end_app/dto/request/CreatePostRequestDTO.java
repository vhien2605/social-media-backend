package com.hien.back_end_app.dto.request;

import com.hien.back_end_app.entities.Comment;
import com.hien.back_end_app.entities.PostMedia;
import com.hien.back_end_app.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequestDTO implements Serializable {
    @NotBlank(message = "content must not be blank")
    private String content;

    private Set<PostMediaRequestDTO> postMedias;
}
