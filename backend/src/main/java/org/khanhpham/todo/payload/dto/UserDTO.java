package org.khanhpham.todo.payload.dto;

import lombok.*;
import org.khanhpham.todo.entity.User;

/**
 * DTO for {@link User}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String username;
}
