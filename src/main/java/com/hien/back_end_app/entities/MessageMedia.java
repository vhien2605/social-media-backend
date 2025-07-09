package com.hien.back_end_app.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "message_media")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageMedia extends AbstractEntity {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "file_url")
    @NotBlank(message = "file media url must not be blank")
    private String fileUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Message message;
}
