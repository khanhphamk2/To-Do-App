package org.khanhpham.todo.service.implement;

import org.khanhpham.todo.common.TokenType;
import org.khanhpham.todo.entity.Token;
import org.khanhpham.todo.exception.CustomException;
import org.khanhpham.todo.payload.dto.TokenDTO;
import org.khanhpham.todo.payload.request.TokenRequest;
import org.khanhpham.todo.repository.TokenRepository;
import org.khanhpham.todo.repository.UserRepository;
import org.khanhpham.todo.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TokenServiceImpl(TokenRepository tokenRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private TokenDTO convertToDTO(Token token) {
        TokenDTO tokenDTO = modelMapper.map(token, TokenDTO.class);
        tokenDTO.setUserId(token.getUser().getId());
        return tokenDTO;
    }

    private Token convertToEntity(TokenRequest tokenRequest) {
        return modelMapper.map(tokenRequest, Token.class);
    }

    /**
     * Generates a secure tokenValue used for password resets.
     *
     * @return A secure random tokenValue encoded in URL-safe Base64 format.
     */
    public String generateToken() {
        SecureRandom random = new SecureRandom();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return uuid + randomPart;
    }

    @Override
    public TokenDTO createToken(Long userId, TokenRequest tokenRequest) {
        Token token = convertToEntity(tokenRequest);
        token.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException(MessageFormat.format("User with id {0} not found", userId))));
        LocalDateTime now = LocalDateTime.now();
        token.setCreatedDate(now);
        token.setUpdatedDate(now);
        return convertToDTO(tokenRepository.save(token));
    }

    @Override
    public TokenDTO updateToken(Long id, TokenRequest tokenRequest) {
        Token token = tokenRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Token not found"));
        token.setType(tokenRequest.getType());
        token.setTokenValue(tokenRequest.getTokenValue());
        token.setExpires(tokenRequest.getExpires());
        token.setUpdatedDate(LocalDateTime.now());
        return convertToDTO(tokenRepository.save(token));
    }

    @Override
    public void deleteToken(Long id) {
        Token token = tokenRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Token not found"));
        tokenRepository.delete(token);
    }

    @Override
    public boolean isTokenExpired(TokenDTO token) {
        return token.getExpires().isBefore(LocalDateTime.now());
    }

    @Override
    public TokenDTO getTokenByTokenValue(String token) {
        return convertToDTO(tokenRepository.findByTokenValue(token));
    }

    @Override
    public TokenDTO getTokenByUserId(Long userId, TokenType type) {
        Token token = tokenRepository.findByUserIdAndType(userId, type);
        if (token != null ) {
            return convertToDTO(token);
        } else {
            return null;
        }
    }
}
