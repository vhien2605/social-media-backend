package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequestDTO implements Serializable {
    @NotBlank(message = "reset token must not be blank")
    private String resetToken;
    @NotBlank(message = "reset password must not be blank")
    private String resetPassword;
}
