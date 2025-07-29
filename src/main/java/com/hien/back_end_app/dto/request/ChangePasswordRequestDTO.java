package com.hien.back_end_app.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class ChangePasswordRequestDTO implements Serializable {
    @NotBlank(message = "token is required")
    private String accessToken;
    @Size(min = 4, message = "new password's size must be greater than or equal to 4")
    private String newPassword;
}
