package com.hien.back_end_app.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class SocketAddMemberRequestDTO {
    private Set<Long> ids;
}
