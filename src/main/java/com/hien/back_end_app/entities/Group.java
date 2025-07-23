package com.hien.back_end_app.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @NotBlank(message = "group name must not be blank")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "group description must not be blank")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @OneToMany(mappedBy = "group")
    private Set<GroupUser> groupUsers;


    @OneToMany(mappedBy = "group")
    private Set<Post> posts;
}
