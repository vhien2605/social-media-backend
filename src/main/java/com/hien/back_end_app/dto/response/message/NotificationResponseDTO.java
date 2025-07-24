package com.hien.back_end_app.dto.response.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hien.back_end_app.utils.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Builder
public class NotificationResponseDTO implements Serializable {
    private long id;
    private String content;
    private NotificationType type;
    private long createdById;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long postId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long commentId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long conversationId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long emotionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long groupId;

    private Date createAt;
    private Date updateAt;
}
