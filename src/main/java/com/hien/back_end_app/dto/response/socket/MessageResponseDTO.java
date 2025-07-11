package com.hien.back_end_app.dto.response.socket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.MessageMedia;
import com.hien.back_end_app.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
