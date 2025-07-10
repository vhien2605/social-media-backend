package com.hien.back_end_app.entities;


import com.hien.back_end_app.utils.anotations.EnumPattern;
import com.hien.back_end_app.utils.enums.EmotionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "emotion")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emotion extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @EnumPattern(name = "emotion_pattern", regexp = "LIKE|HAHA|CRY|COOL|LOVE|MAD")
    private EmotionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
