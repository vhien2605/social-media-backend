package com.hien.back_end_app.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class CreateGroupPostRequestDTO implements Serializable {
    @NotNull(message = "group id must not be null")
    private long groupId;
    @NotBlank(message = "content must not be blank")
    private String content;
    Set<GroupPostMediaDTO> medias;
}
