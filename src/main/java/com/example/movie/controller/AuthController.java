package com.example.movie.controller;

import com.example.movie.auth.Service.JWTService;
import com.example.movie.auth.Service.RefreshTokenService;
import com.example.movie.auth.entity.RefreshToken;
import com.example.movie.auth.entity.User;
import com.example.movie.auth.utils.AuthResponse;
import com.example.movie.auth.utils.LoginRequest;
import com.example.movie.auth.utils.RefreshTokenRequest;
import com.example.movie.auth.utils.RegisterRequest;
import com.example.movie.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JWTService jwtService;
    public AuthController(AuthService authService , RefreshTokenService refreshTokenService, JWTService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        RefreshToken refreshToken = refreshTokenService.verfiyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .build()
        );

    }

}
