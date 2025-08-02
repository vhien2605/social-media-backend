package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessageDTO implements Serializable {

    @NotBlank(message = "content must not be blank")
    private String content;
    private SocketMessageMediaDTO socketMessageMediaDTO;
}
