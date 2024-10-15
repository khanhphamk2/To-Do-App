package org.khanhpham.todo.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    @NotEmpty(message = "Email should not be null or empty")
    @Email(message = "Email should be valid")
    private String email;
    private String password;
}
