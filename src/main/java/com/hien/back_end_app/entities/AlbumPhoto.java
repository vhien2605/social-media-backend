package com.hien.back_end_app.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "album_photo")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumPhoto extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "image_url")
    @NotBlank(message = "image url must not be blank")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;
}
