package com.example.user_service.dtos.requests;
import com.example.user_service.models.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String nomPrenom;
    private Role role;

    // Optionnels selon le profil
    private String universite;
    private String competences;
    private String rccm;
    private String secteur;
    private String description;
}