package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class CreateGroupRequestDTO implements Serializable {
    @NotBlank(message = "group name must not be blank")
    private String name;

    @NotBlank(message = "group description must not be blank")
    private String description;
}
