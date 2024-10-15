package org.khanhpham.todo.service;

import jakarta.servlet.http.HttpServletRequest;
import org.khanhpham.todo.payload.request.*;
import org.khanhpham.todo.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse loginWithIdentityAndPassword(LoginRequest loginRequest);
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse loginWithGoogle(HttpServletRequest request, LoginGoogleRequest loginGoogleRequest);
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    void resetPassword(String resetPasswordToken, ResetPasswordRequest resetPasswordRequest);
}
