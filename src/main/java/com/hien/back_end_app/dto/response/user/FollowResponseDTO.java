package com.hien.back_end_app.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class FollowResponseDTO implements Serializable {
    private long followId;
    private String imageUrl;
    private String fullName;
}
