package com.hien.back_end_app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(name = "conversation")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_group")
    private boolean isGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User user;

    @ManyToMany
    @JoinTable(name = "participant_conversation", joinColumns = @JoinColumn(name = "conversation_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants;
    
    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages;
}
