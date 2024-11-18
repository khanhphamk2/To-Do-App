package org.khanhpham.todo.repository;

import org.khanhpham.todo.common.TokenType;
import org.khanhpham.todo.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByUserIdAndType(Long userId, TokenType type);
    Token findByTokenValue(String token);
}