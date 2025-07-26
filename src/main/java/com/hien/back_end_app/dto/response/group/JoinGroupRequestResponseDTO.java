package com.hien.back_end_app.dto.response.group;

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
import java.util.Date;


@Setter
@Getter
@Builder
public class JoinGroupRequestResponseDTO implements Serializable {
    private long id;
    private long createdBy;
    private Group group;
    private RequestStatus status;
    private String message;
    private Date createAt;
}
