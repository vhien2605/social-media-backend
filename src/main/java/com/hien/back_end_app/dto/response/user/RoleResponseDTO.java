package com.hien.back_end_app.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@Builder
public class RoleResponseDTO implements Serializable {
    private String name;
    private String description;
}
