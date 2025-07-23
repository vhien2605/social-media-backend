package com.hien.back_end_app.entities;


import com.hien.back_end_app.utils.anotations.EnumPattern;
import com.hien.back_end_app.utils.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "upload_post_request")
public class UploadPostRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @EnumPattern(name = "request_status", regexp = "PENDING|ACCEPTED||REJECTED")
    private RequestStatus status;

    @Column(name = "content")
    @NotBlank(message = "content must not be blank")
    private String content;

    @OneToMany(mappedBy = "postRequest")
    Set<PostRequestMedia> medias;
}
