package org.khanhpham.todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get user by identity",
            description = "Retrieve user details by identity"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("{identity}")
    public ResponseEntity<UserDTO> getUserByIdentity(
            @Parameter(description = "The identity of the user to be retrieved") @PathVariable String identity){
        return ResponseEntity.ok(userService.findByIdentity(identity));
    }
}
