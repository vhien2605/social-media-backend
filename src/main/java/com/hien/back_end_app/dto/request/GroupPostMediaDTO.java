package com.hien.back_end_app.dto.request;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPostMediaDTO implements Serializable {
    private String name;
    private String type;
    private String base64Data;
}
