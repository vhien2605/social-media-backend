package com.hien.back_end_app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receiver_notification")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiverNotification extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiverUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;
}
