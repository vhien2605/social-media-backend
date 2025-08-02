package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO implements Serializable {
    @NotNull(message = "email must not be null")
    private String email;
    @NotBlank(message = "password must not be blank")
    private String password;
    @NotBlank(message = "fullName must not be blank")
    private String fullName;
}
