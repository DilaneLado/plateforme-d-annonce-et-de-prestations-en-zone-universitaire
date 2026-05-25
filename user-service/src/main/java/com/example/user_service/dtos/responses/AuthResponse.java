package com.example.user_service.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;          // Le token de sécurité
    private String tokenType;      // Ce sera "Bearer"
    private String email;
    private String role;
}
