package com.hien.back_end_app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "album")
public class

Album extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    @NotBlank(message = "title must not be blank")
    private String title;

    @Column(name = "description")
    @NotBlank(message = "description must not be blank")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "album")
    private Set<AlbumPhoto> albumPhotos;
}
