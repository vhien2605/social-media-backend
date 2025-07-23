package com.hien.back_end_app.entities;


import com.hien.back_end_app.utils.anotations.EnumPattern;
import com.hien.back_end_app.utils.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "join_group_request")
public class JoinGroupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @EnumPattern(name = "request_status", regexp = "PENDING|ACCEPTED|REJECTED")
    private RequestStatus status;

    @Column(name = "message")
    @NotBlank(message = "message must not be blank")
    private String message;
}
