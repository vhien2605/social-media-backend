package com.hien.back_end_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class CreateConversationRequestDTO implements Serializable {
    @NotBlank(message = "name must not be blank")
    private String name;
    @NotEmpty(message = "participants must not be empty")
    private Set<Long> participants;
}
