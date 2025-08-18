package com.hien.back_end_app.dto.response.report;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hien.back_end_app.dto.response.post.UserRenderResponseDTO;
import com.hien.back_end_app.utils.enums.ReasonType;
import com.hien.back_end_app.utils.enums.ReportType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Builder
public class ReportResponseDTO implements Serializable {
    private Long id;
    private ReportType reportType;
    private ReasonType reasonType;
    private String description;
    private UserRenderResponseDTO createdUser;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long postId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long commentId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long groupId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createAt;
}
