package com.hien.back_end_app.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class GroupPostMediaDTO implements Serializable {
    private String name;
    private String type;
    private String base64Data;
}
