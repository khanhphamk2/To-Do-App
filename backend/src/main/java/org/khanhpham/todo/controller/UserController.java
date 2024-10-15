package org.khanhpham.todo.controller;

import org.khanhpham.todo.payload.dto.UserDTO;
import org.khanhpham.todo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.data.rest.base-path}/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{identity}")
    public ResponseEntity<UserDTO> getUserByIdentity(@PathVariable String identity){
        return ResponseEntity.ok(userService.findByIdentity(identity));
    }
}
