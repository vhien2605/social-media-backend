package com.hien.back_end_app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "refresh_token")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token extends AbstractEntity {
    @Id
    private String jti;
    private String email;
}
