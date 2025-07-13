package com.hien.back_end_app.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class PageResponseDTO<T> implements Serializable {
    private long pageNo;
    private long pageSize;
    private long totalPage;
    private T data;
}
