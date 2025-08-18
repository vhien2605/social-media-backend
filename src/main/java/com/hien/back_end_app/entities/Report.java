package com.hien.back_end_app.entities;

import com.hien.back_end_app.utils.enums.ReasonType;
import com.hien.back_end_app.utils.enums.ReportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "report")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull(message = "type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type")
    private ReportType reportType;

    @NotNull(message = "type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "reason_type")
    private ReasonType reasonType;

    @NotBlank(message = "description is required")
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_id")
    private User createdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
}
