package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class LogoutRequestDTO implements Serializable {
    @NotBlank(message = "token must be required")
    private String accessToken;
    @NotBlank(message = "token must be required")
    private String refreshToken;
}
