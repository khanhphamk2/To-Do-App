package org.khanhpham.todo.config;

import org.khanhpham.todo.security.JwtAuthenticationEntryPoint;
import org.khanhpham.todo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(
            JwtAuthenticationEntryPoint authenticationEntryPoint,
            JwtAuthenticationFilter authenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    /**
     * Bean definition for the password encoder.
     * The password encoder uses BCrypt hashing algorithm for securely encoding passwords.
     *
     * @return a {@link BCryptPasswordEncoder} instance
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean definition for the {@link AuthenticationManager}, which is responsible for managing
     * authentication in the application. It is used by Spring Security to authenticate users.
     *
     * @param configuration the {@link AuthenticationConfiguration} for Spring Security
     * @return an {@link AuthenticationManager} instance
     * @throws Exception if an error occurs while obtaining the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Bean definition for the security filter chain. This configures the security rules
     * and settings for handling HTTP requests.
     *
     * <ul>
     *   <li>CSRF is disabled since the application is stateless.</li>
     *   <li>CORS is enabled with default settings.</li>
     *   <li>GET requests to the "/api/v1/**" endpoints are permitted without authentication.</li>
     *   <li>Authentication is required for all other requests.</li>
     *   <li>A stateless session policy is enforced, meaning no session is created or used.</li>
     *   <li>The custom JWT authentication filter is added before the default username-password filter.</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object to configure the security settings
     * @return a {@link SecurityFilterChain} object representing the configured security chain
     * @throws Exception if an error occurs during the configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .anyRequest().authenticated()
                ).exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        // Add custom authentication filter before the default UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
