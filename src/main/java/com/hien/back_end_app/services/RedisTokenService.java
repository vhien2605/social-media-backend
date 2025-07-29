package com.hien.back_end_app.services;


import com.hien.back_end_app.entities.RedisToken;
import com.hien.back_end_app.repositories.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTokenRepository tokenRepository;

    public RedisToken saveToken(RedisToken access) {
        return tokenRepository.save(access);
    }

    public Optional<RedisToken> findByJti(String jti) {
        return tokenRepository.findById(jti);
    }
}
