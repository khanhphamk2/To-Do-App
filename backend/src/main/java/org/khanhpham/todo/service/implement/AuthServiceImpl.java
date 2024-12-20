package org.khanhpham.todo.service.implement;

import jakarta.servlet.http.HttpServletRequest;
import org.khanhpham.todo.common.TokenType;
import org.khanhpham.todo.entity.User;
import org.khanhpham.todo.exception.CustomException;
import org.khanhpham.todo.payload.dto.TokenDTO;
import org.khanhpham.todo.payload.dto.UserDTO;
import org.khanhpham.todo.payload.request.*;
import org.khanhpham.todo.payload.response.AuthResponse;
import org.khanhpham.todo.payload.response.UserInfoResponse;
import org.khanhpham.todo.repository.UserRepository;
import org.khanhpham.todo.security.JwtTokenProvider;
import org.khanhpham.todo.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * AuthServiceImpl implements the AuthService interface and provides authentication services,
 * including user login with identity and password, registration, login with Google OAuth,
 * and password reset functionalities.
 * *
 * It integrates with Spring Security for authentication management and uses JWT (Json Web Token)
 * for securing endpoints. It also communicates with the user repository to manage user data and
 * sends email notifications via the EmailService.
 * *
 * Dependencies injected include:
 * - AuthenticationManager: For authenticating users during login.
 * - UserRepository: For accessing and persisting user data.
 * - PasswordEncoder: For encrypting user passwords.
 * - JwtTokenProvider: For generating and validating JWT tokens.
 * - UserService: For user-related operations, such as fetching or creating users.
 * - EmailService: For sending emails during password reset operations.
 * - ModelMapper: For mapping between entity objects and DTOs.
 * *
 * The class includes methods to:
 * - Authenticate users with a username or email and password.
 * - Register new users and ensure valid password formatting.
 * - Authenticate users using Google OAuth tokens.
 * - Handle password reset flows, including generating reset tokens and updating passwords.
 *
 * @see AuthService
 * @see JwtTokenProvider
 * @see UserService
 * @see EmailService
 */

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${frontend.url}")
    private String frontEndUrl;
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                           UserService userService, TokenService tokenService, EmailService emailService,
                            UserDetailsService userDetailsService, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
    }

    /**
     * Logs in the user using their identity (username or email) and password.
     *
     * @param loginRequest Contains the identity (username or email) and password for authentication.
     * @return AuthResponse containing the JWT tokenValue and user information if authentication is successful.
     * @throws CustomException if the username or password is incorrect.
     */
    @Override
    public AuthResponse loginWithIdentityAndPassword(LoginRequest loginRequest) {
        String identity = loginRequest.getIdentity();

        UserDTO user = userService.findByUsernameOrEmail(identity, identity);

        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect!");
        }

        TokenDTO oldRefreshToken = tokenService.getTokenByUserId(user.getId(), TokenType.REFRESH);
        if (oldRefreshToken != null) {
            tokenService.deleteToken(oldRefreshToken.getId());
        }

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        TokenRequest tokenRequest = new TokenRequest(
                    newRefreshToken,
                    TokenType.REFRESH,
                    LocalDateTime.now().plusDays(14)
        );
        TokenDTO refreshToken = tokenService.createToken(user.getId(), tokenRequest);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                identity, loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(jwtTokenProvider.generateAccessToken(authentication));
        authResponse.setUser(user);
        authResponse.setRefreshToken(refreshToken.getTokenValue());

        return authResponse;
    }

    /**
     * Registers a new user by checking if the username and email are unique, validating the password,
     * and then saving the user in the database.
     *
     * @param registerRequest Contains registration data such as username, email, and password.
     * @return AuthResponse with a JWT tokenValue and user details after successful registration.
     * @throws CustomException if the username or email already exists or the password is invalid.
     */
    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        // add check for username exists in database
        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerRequest.getUsername()))) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        // add check for email exists in database
        if (Boolean.TRUE.equals(userRepository.existsByEmail(registerRequest.getEmail()))) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        String password = registerRequest.getPassword();
        if (!isPasswordValid(password)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid password format.");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        return loginWithIdentityAndPassword(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword()));
    }

    @Override
    public AuthResponse refreshTokens(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh tokenValue.");
        }

        // Extract identity from refresh tokenValue
        String identity = jwtTokenProvider.getIdentityFromRefreshToken(refreshToken);

        UserDTO user = userService.findByUsernameOrEmail(identity, identity);
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "User not found.");
        }

        // Retrieve old refresh tokenValue
        TokenDTO oldRefreshToken = tokenService.getTokenByTokenValue(refreshToken);

        // Load user details and set authentication
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        TokenRequest tokenRequest = new TokenRequest(newRefreshToken, TokenType.REFRESH, LocalDateTime.now().plusDays(30));
        TokenDTO refreshTokenDTO = tokenService.updateToken(oldRefreshToken.getId(), tokenRequest);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(newAccessToken);
        authResponse.setUser(user);
        authResponse.setRefreshToken(refreshTokenDTO.getTokenValue());

        return authResponse;
    }

    /**
     * Logs in a user with their Google OAuth access tokenValue by fetching their profile data
     * from the Google API and verifying if they are already registered in the system.
     *
     * @param request The HttpServletRequest containing request details.
     * @param loginGoogleRequest Contains the Google OAuth access tokenValue.
     * @return AuthResponse containing the JWT tokenValue and user details.
     * @throws CustomException if user info cannot be retrieved or tokenValue is invalid.
     */
    @Override
    public AuthResponse loginWithGoogle(HttpServletRequest request, LoginGoogleRequest loginGoogleRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loginGoogleRequest.getAccessToken());

        ResponseEntity<UserInfoResponse> responseEntity = restTemplate.exchange(
                USER_INFO_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserInfoResponse.class
        );
        UserInfoResponse userInfoResponse = responseEntity.getBody();

        if (userInfoResponse == null) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get user info");
        }

        String email = userInfoResponse.getEmail();

        var user = userRepository.findByEmail(email);

        if (user == null) {
            String username = extractUsernameFromEmail(email);
            user = userService.createUser(email, username);
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            userRepository.save(user);
        }

        UserDetails userDetails = buildUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setUser(convertToDTO(user));
        authResponse.setAccessToken(jwtTokenProvider.generateAccessToken(authentication));

        return authResponse;
    }


    /**
     * Initiates the password reset process by generating a reset tokenValue, saving it to the user,
     * and sending a reset link via email.
     *
     * @param forgotPasswordRequest Contains the email address of the user requesting the password reset.
     * @throws CustomException if the email does not exist or any internal error occurs.
     */
    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        try {
            String email = forgotPasswordRequest.getEmail();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Email is not exists!.");
            }

            // Generate tokenValue
            String token = tokenService.generateToken();

            // Set expired date 15 minutes
            LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(15);

            TokenRequest tokenRequest = new TokenRequest(token, TokenType.RESET_PASSWORD, expiredDate);
            TokenDTO resetToken = tokenService.createToken(user.getId(), tokenRequest);

            // Send email
            String subject = "Reset password";
            // front end url
            String content = "Please click the link below to reset your password: \n"
                    + frontEndUrl + "/reset-password/" + "?tokenValue=" + resetToken.getTokenValue();
            emailService.sendEmail(email, subject, content);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Resets the user's password using a tokenValue provided by the password reset email.
     *
     * @param resetPasswordToken The reset password tokenValue sent via email.
     * @param resetPasswordRequest Contains the new password for the user.
     * @throws CustomException if the tokenValue is invalid, expired, or any other error occurs during reset.
     */
    @Override
    public void resetPassword(String resetPasswordToken, ResetPasswordRequest resetPasswordRequest) {
        try {
            String password = resetPasswordRequest.getPassword();

            TokenDTO resetToken = tokenService.getTokenByTokenValue(resetPasswordToken);

            UserDTO userDTO = userService.findById(resetToken.getUserId());

            if (userDTO == null) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Token is invalid!.");
            }
            LocalDateTime expiredDate = resetToken.getExpires();

            if (LocalDateTime.now().isAfter(expiredDate)) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Token is expired!.");
            }
            userService.changePassword(userDTO.getUsername(), password);
            tokenService.deleteToken(resetToken.getId());

        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    /**
     * Builds UserDetails for Spring Security from the user entity.
     *
     * @param user The user entity from the database.
     * @return UserDetails containing the user's credentials and authorities.
     */
    private UserDetails buildUserDetails(User user) {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities
        );
    }

    /**
     * Converts a User entity to a UserDTO using ModelMapper for data transfer.
     *
     * @param user The user entity.
     * @return UserDTO containing user data.
     */
    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * Extracts the username from the user's email address by taking the part before the '@' symbol.
     *
     * @param email The user's email address.
     * @return The username extracted from the email address.
     */
    private String extractUsernameFromEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(0, atIndex);
        }
        return email;
    }

    /**
     * Validates the user's password based on a regex pattern requiring at least 8 characters,
     * including uppercase letters, lowercase letters, and numbers.
     *
     * @param password The password to validate.
     * @return true if the password matches the validation criteria; false otherwise.
     */
    private boolean isPasswordValid(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";
        return password.matches(passwordRegex);
    }


}
