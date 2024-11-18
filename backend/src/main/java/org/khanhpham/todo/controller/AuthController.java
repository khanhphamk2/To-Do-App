package org.khanhpham.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.khanhpham.todo.payload.request.*;
import org.khanhpham.todo.payload.response.AuthResponse;
import org.khanhpham.todo.service.AuthService;
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

    @PostMapping(value = {"/login", "/sign-in"})
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        System.out.println("Login request: " + loginRequest);
        return ResponseEntity.ok(authService.loginWithIdentityAndPassword(loginRequest));
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshTokens(@RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(authService.refreshTokens(request));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(HttpServletRequest request, @RequestBody LoginGoogleRequest loginGoogleRequest){
        return ResponseEntity.ok(authService.loginWithGoogle(request, loginGoogleRequest));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest request){
        authService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam(value = "token") String token, @RequestBody ResetPasswordRequest request){
        authService.resetPassword(token, request);
        return ResponseEntity.ok().build();
    }
}
