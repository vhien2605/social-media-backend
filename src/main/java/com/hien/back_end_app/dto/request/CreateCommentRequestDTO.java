package com.hien.back_end_app.dto.request;


import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequestDTO implements Serializable {
    private String content;
}
