package org.khanhpham.todo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.khanhpham.todo.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-refresh-secret}")
    private String jwtRefreshSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationMillis;

    @Value("${app-jwt-refresh-expiration-milliseconds}")
    private long jwtRefreshExpirationMillis;

    private static final String INVALID_TOKEN = "Invalid JWT tokenValue";
    private static final String EXPIRED_TOKEN = "Expired JWT tokenValue";
    private static final String UNSUPPORTED_TOKEN = "Unsupported JWT tokenValue";
    private static final String EMPTY_CLAIMS = "JWT claims string is empty";

    private Key accessKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    private Key refreshKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateToken(userPrincipal.getUsername(), jwtExpirationMillis, accessKey());
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, jwtRefreshExpirationMillis, refreshKey());
    }

    // Generate tokenValue (generic)
    private String generateToken(String username, long expirationMillis, Key key) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // Validate tokenValue (generic)
    private boolean validateToken(String token, Key key) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, EMPTY_CLAIMS);
        }
    }

    // Validate Access Token
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessKey());
    }

    // Validate Refresh Token
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshKey());
    }

    // Get identity from tokenValue (generic)
    private String getIdentityFromToken(String token, Key key) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Get identity from Access Token
    public String getIdentityFromAccessToken(String token) {
         return getIdentityFromToken(token, accessKey());
    }

    // Get identity from Refresh Token
    public String getIdentityFromRefreshToken(String token) {
        return getIdentityFromToken(token, refreshKey());
    }
}
