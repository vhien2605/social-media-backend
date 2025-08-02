package com.hien.back_end_app.dto.request;

import com.hien.back_end_app.entities.Role;
import com.hien.back_end_app.utils.anotations.EnumPattern;
import com.hien.back_end_app.utils.enums.AuthProvider;
import com.hien.back_end_app.utils.enums.Gender;
import com.hien.back_end_app.utils.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequestDTO implements Serializable {
    @NotNull(message = "email must not be null")
    private String email;
    @NotBlank(message = "password must not be blank")
    private String password;
    @NotBlank(message = "name must not be blank")
    private String fullName;
    private Date dateOfBirth;
    private String address;
    private String education;
    private String work;
    private Gender gender;
}
