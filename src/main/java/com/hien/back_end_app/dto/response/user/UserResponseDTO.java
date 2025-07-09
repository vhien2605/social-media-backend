package com.hien.back_end_app.dto.response.user;

import com.hien.back_end_app.entities.Role;
import com.hien.back_end_app.utils.enums.AuthProvider;
import com.hien.back_end_app.utils.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Builder
public class UserResponseDTO implements Serializable {
    private long id;
    private String email;
    private AuthProvider authProvider;
    private String providerId;
    private UserStatus userStatus;
    private String fullName;
    private String imageUrl;
    private Date dateOfBirth;
    private String address;
    private String education;
    private String work;
    private Set<Role> roles;
}
