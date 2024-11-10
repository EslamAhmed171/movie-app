package com.example.movie.auth.Service;

import com.example.movie.auth.Repository.RefreshTokenRepository;
import com.example.movie.auth.Repository.UserRepository;
import com.example.movie.auth.entity.RefreshToken;
import com.example.movie.auth.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found with this email: " + username));

        RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiryTime(Instant.now().plusMillis(50 * 3600 * 1000))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }
    public RefreshToken verfiyRefreshToken(String refreshToken) {
        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found!"));

        if (refToken.getExpiryTime().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh token expired");
        }
        return refToken;
    }
}
