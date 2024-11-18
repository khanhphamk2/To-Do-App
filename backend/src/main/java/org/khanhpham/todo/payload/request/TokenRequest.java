package org.khanhpham.todo.payload.request;

import lombok.*;
import org.khanhpham.todo.common.TokenType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {
    private String tokenValue;
    private TokenType type;
    private LocalDateTime expires;
}
