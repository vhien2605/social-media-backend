package com.hien.back_end_app.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class CreateCommentRequestDTO implements Serializable {
    private String content;
}
