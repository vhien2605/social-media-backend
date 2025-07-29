package com.hien.back_end_app.services;


import com.hien.back_end_app.entities.Token;
import com.hien.back_end_app.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public void deleteById(String id) {
        tokenRepository.deleteById(id);
    }

    public Optional<Token> findById(String id) {
        return tokenRepository.findById(id);
    }
}
