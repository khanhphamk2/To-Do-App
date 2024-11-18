package org.khanhpham.todo.service;

import org.khanhpham.todo.common.TokenType;
import org.khanhpham.todo.payload.dto.TokenDTO;
import org.khanhpham.todo.payload.request.TokenRequest;

public interface TokenService {
    TokenDTO createToken(Long userId, TokenRequest tokenRequest);
    TokenDTO updateToken(Long id, TokenRequest tokenRequest);
    void deleteToken(Long id);
    String generateToken();
    boolean isTokenExpired(TokenDTO token);
    TokenDTO getTokenByUserId(Long userId, TokenType type);
    TokenDTO getTokenByTokenValue(String token);
}
