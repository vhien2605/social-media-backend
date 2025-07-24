package com.hien.back_end_app.dto.response.group;

import com.hien.back_end_app.entities.GroupUser;
import com.hien.back_end_app.entities.Post;
import com.hien.back_end_app.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class GroupResponseDTO implements Serializable {
    private long id;
    private String name;
    private String description;
    private long createdId;
}
