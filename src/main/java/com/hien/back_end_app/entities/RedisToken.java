package com.hien.back_end_app.entities;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("Token")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisToken {
    private String jti;
    private String email;
    @TimeToLive
    private Long timeToLive;
}
