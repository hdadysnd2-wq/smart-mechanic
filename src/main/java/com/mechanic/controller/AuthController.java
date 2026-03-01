package com.mechanic.controller;

import com.mechanic.dto.request.LoginRequest;
import com.mechanic.dto.response.ApiResponse;
import com.mechanic.dto.response.AuthResponse;
import com.mechanic.entity.UserEntity;
import com.mechanic.repository.UserRepository;
import com.mechanic.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          JwtTokenProvider tokenProvider,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("بيانات الدخول غير صحيحة"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("بيانات الدخول غير صحيحة");
        }

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole().name());
        String refresh = tokenProvider.generateRefreshToken(user.getEmail());

        return ResponseEntity.ok(ApiResponse.ok("تم تسجيل الدخول",
                AuthResponse.of(token, refresh, 3600000L,
                        user.getEmail(), user.getFullName(), user.getRole().name())));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody LoginRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("البريد الإلكتروني مستخدم بالفعل");
        }

        var user = new UserEntity();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullName(request.email());
        userRepository.save(user);

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole().name());
        String refresh = tokenProvider.generateRefreshToken(user.getEmail());

        return ResponseEntity.status(201).body(ApiResponse.ok("تم إنشاء الحساب",
                AuthResponse.of(token, refresh, 3600000L,
                        user.getEmail(), user.getFullName(), user.getRole().name())));
    }
}
