package org.khanhpham.todo.service;

import org.khanhpham.todo.entity.User;
import org.khanhpham.todo.payload.dto.UserDTO;

public interface UserService {
    UserDTO findByIdentity(String identity);
    UserDTO findById(Long id);
    User createUser(String email, String username);
    UserDTO findByUsernameOrEmail(String email, String username);
}
