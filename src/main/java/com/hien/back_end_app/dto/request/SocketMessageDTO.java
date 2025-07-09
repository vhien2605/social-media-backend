package com.hien.back_end_app.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Builder
public class SocketMessageDTO implements Serializable {
    private String content;
    private SocketMessageMediaDTO socketMessageMediaDTO;
}
