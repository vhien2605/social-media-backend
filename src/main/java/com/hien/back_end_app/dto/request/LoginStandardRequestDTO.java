package com.hien.back_end_app.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginStandardRequestDTO implements Serializable {
    @NotBlank(message = "email must not blank")
    @Email
    private String email;

    @NotBlank(message = "password must not blank")
    @Size(min = 6, message = "password length must be greater than or equal 6")
    private String password;
}
