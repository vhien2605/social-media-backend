package com.hien.back_end_app.dto.request;

import com.hien.back_end_app.utils.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInformationRequestDTO implements Serializable {
    @NotNull(message = "userId must not be null")
    private long userId;
    private String fullName;
    private Date dateOfBirth;
    private String address;
    private String education;
    private String work;
    private Gender gender;
}
