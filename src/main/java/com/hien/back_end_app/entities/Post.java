package com.hien.back_end_app.entities;


import com.hien.back_end_app.repositories.specification.SupportsSpecification;
import com.hien.back_end_app.utils.anotations.EnumPattern;
import com.hien.back_end_app.utils.enums.PostType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "post")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Post extends AbstractEntity implements SupportsSpecification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "content")
    @NotBlank(message = "content must not be blank")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @EnumPattern(name = "post_type", regexp = "WALL_POST|GROUP_POST")
    private PostType type;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post")
    private Set<PostMedia> postMedias;

    @OneToMany(mappedBy = "post")
    private Set<Emotion> emotions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
}
