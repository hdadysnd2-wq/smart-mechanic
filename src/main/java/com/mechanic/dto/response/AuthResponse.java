package com.mechanic.dto.response;
public record AuthResponse(String accessToken, String refreshToken, String tokenType, long expiresIn, String email, String fullName, String role) {
    public static AuthResponse of(String access, String refresh, long exp, String email, String name, String role) {
        return new AuthResponse(access, refresh, "Bearer", exp, email, name, role);
    }
}
