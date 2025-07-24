package com.hien.back_end_app.dto.request;

import com.hien.back_end_app.entities.Group;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.utils.anotations.EnumPattern;
import com.hien.back_end_app.utils.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class JoinGroupRequestDTO implements Serializable {
    private long groupId;
    @NotBlank(message = "message must not be blank")
    private String message;
}
