package com.hien.back_end_app.dto.request;

import com.hien.back_end_app.utils.enums.ReasonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportRequestDTO implements Serializable {
    @NotNull(message = "reason type must not be null")
    private ReasonType reasonType;
    @NotBlank(message = "description must not be blank")
    private String description;
    private Long postId;
    private Long commentId;
    private Boolean isPostReport;
}
