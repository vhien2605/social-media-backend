package com.hien.back_end_app.dto.response.message;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hien.back_end_app.dto.response.post.UserRenderResponseDTO;
import com.hien.back_end_app.utils.enums.NotificationType;
import com.hien.back_end_app.utils.validators.ZeroFilter;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDetailResponseDTO implements Serializable {
    private long id;
    private String content;
    private NotificationType type;
    private long createdById;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ZeroFilter.class)
    private long postId;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ZeroFilter.class)
    private long commentId;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ZeroFilter.class)
    private long conversationId;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ZeroFilter.class)
    private long emotionId;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ZeroFilter.class)
    private long groupId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updateAt;
    
    private Set<UserRenderResponseDTO> receivers;
}
