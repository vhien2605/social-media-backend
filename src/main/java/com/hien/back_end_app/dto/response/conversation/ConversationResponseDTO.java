package com.hien.back_end_app.dto.response.conversation;

import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import com.hien.back_end_app.entities.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Setter
@Getter
@Builder
public class ConversationResponseDTO {
    private long id;
    private String name;
    private boolean isGroup;
    private UserResponseDTO user;
    private Set<UserResponseDTO> participants;
}
