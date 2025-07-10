package com.hien.back_end_app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "follow")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private User targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_id")
    private User followUser;
}
