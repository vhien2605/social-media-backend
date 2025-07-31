package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class ForgotPasswordRequestDTO implements Serializable {
    @NotBlank(message = "register email is required")
    private String registerEmail;
}
