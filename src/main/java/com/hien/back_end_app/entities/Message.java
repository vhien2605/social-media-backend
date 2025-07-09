package com.hien.back_end_app.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Setter
@Getter
@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "content")
    @NotBlank(message = "content message must not be blank")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private User sourceUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    
    @OneToOne(mappedBy = "message", fetch = FetchType.LAZY)
    private MessageMedia messageMedia;
}
