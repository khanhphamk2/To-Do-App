package org.khanhpham.todo.payload.dto;

import lombok.*;
import org.khanhpham.todo.common.TokenType;
import org.khanhpham.todo.entity.Token;

import java.time.LocalDateTime;

/**
 * DTO for {@link Token}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenDTO extends AudiDTO {
    Long id;
    String tokenValue;
    TokenType type;
    LocalDateTime expires;
    Long userId;
}