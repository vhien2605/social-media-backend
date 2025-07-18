package com.hien.back_end_app.dto.response.message;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@Builder
public class MessageResponseDTO {
    private long id;
    private String content;
    private long sourceId;
    private long conversationId;
    private MediaResponseDTO messageMedia;
    private Date createAt;
    private Date updateAt;
}
