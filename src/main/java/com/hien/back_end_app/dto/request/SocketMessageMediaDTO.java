package com.hien.back_end_app.dto.request;

import com.hien.back_end_app.entities.Message;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessageMediaDTO implements Serializable {
    private String name;
    private String type;
    private String base64Data;
}


