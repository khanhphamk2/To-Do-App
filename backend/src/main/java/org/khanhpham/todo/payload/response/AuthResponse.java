package org.khanhpham.todo.payload.response;

import lombok.*;
import org.khanhpham.todo.payload.dto.UserDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {
    private UserDTO user;
    private String accessToken;
    private String refreshToken;
}
