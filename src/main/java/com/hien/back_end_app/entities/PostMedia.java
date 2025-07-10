package com.hien.back_end_app.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "post_media")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "file_url")
    @NotBlank(message = "file_url must not be blank")
    private String fileUrl;
}
