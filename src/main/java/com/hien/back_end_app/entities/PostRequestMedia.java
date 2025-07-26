package com.hien.back_end_app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_request_media")
public class PostRequestMedia extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_request_id")
    private UploadPostRequest postRequest;

    @Column(name = "file_url")
    @NotBlank(message = "file_url must not be blank")
    private String fileUrl;
}
