package com.hien.back_end_app.dto.request;

import com.hien.back_end_app.entities.Role;
import com.hien.back_end_app.utils.anotations.EnumPattern;
import com.hien.back_end_app.utils.enums.AuthProvider;
import com.hien.back_end_app.utils.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@Setter
@Getter
@Builder
public class UserCreationRequestDTO implements Serializable {
    @NotNull(message = "email must not be null")
    private String email;

    @NotBlank(message = "password must not be blank")
    private String password;

    @NotBlank(message = "name must not be blank")
    private String fullName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "education")
    private String education;

    @Column(name = "work")
    private String work;
}
