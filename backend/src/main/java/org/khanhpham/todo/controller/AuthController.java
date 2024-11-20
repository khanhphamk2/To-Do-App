package org.khanhpham.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.khanhpham.todo.payload.request.*;
import org.khanhpham.todo.payload.response.AuthResponse;
import org.khanhpham.todo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.data.rest.base-path}/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login user", description = "Authenticate user with username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    })
    @PostMapping(value = {"/login", "/sign-in"})
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Parameter(description = "User login credentials") LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.loginWithIdentityAndPassword(loginRequest));
    }

    @Operation(summary = "Register user", description = "Create a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid registration data")
    })
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Parameter(description = "User registration details") RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
    }

    @Operation(summary = "Refresh tokens", description = "Generate new access and refresh tokens using a refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully refreshed tokens",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid refresh token")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshTokens(
            @RequestBody @Parameter(description = "Refresh token request") RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshTokens(request));
    }

    @Operation(summary = "Login with Google", description = "Authenticate user using Google OAuth2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in with Google",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid Google login data")
    })
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(
            @Parameter(description = "HTTP servlet request object to capture Google login context") HttpServletRequest request,
            @RequestBody @Parameter(description = "Google login credentials") LoginGoogleRequest loginGoogleRequest) {
        return ResponseEntity.ok(authService.loginWithGoogle(request, loginGoogleRequest));
    }

    @Operation(summary = "Forgot password", description = "Send a password reset link to the user's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset link sent successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid email format")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody @Parameter(description = "User email for password recovery") ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset password", description = "Reset the user's password using a reset token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully reset"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid reset token or password format"),
            @ApiResponse(responseCode = "404", description = "Reset token not found")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestParam(value = "token") @Parameter(description = "Password reset token from the email") String token,
            @RequestBody @Parameter(description = "New password and confirmation") ResetPasswordRequest request) {
        authService.resetPassword(token, request);
        return ResponseEntity.ok().build();
    }
}
