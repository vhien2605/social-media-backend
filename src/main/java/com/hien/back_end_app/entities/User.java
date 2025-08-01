package com.hien.back_end_app.entities;

import com.hien.back_end_app.repositories.specification.SupportsSpecification;
import com.hien.back_end_app.utils.anotations.EnumPattern;
import com.hien.back_end_app.utils.enums.AuthProvider;
import com.hien.back_end_app.utils.enums.Gender;
import com.hien.back_end_app.utils.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AbstractEntity implements SupportsSpecification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    @NotNull(message = "email must not be null")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    @EnumPattern(name = "auth_provider", regexp = "GOOGLE|FACEBOOK|STANDARD")
    private AuthProvider authProvider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    @EnumPattern(name = "user_status", regexp = "ONLINE|OFFLINE")
    private UserStatus userStatus;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    @EnumPattern(name = "user_gender", regexp = "MALE|FEMALE")
    private Gender gender;

    @Column(name = "full_name")
    @NotBlank(message = "name must not be blank")
    private String fullName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "education")
    private String education;

    @Column(name = "work")
    private String work;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "createdBy")
    private Set<Post> posts;

    @OneToMany(mappedBy = "targetUser")
    private Set<Follow> follows;

    @OneToMany(mappedBy = "user")
    private Set<Album> albums;

    @OneToMany(mappedBy = "user")
    private Set<Conversation> conversations;
}
