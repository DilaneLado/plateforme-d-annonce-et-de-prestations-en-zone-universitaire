package com.example.user_service.controllers;

import com.example.user_service.dtos.requests.LoginRequest;
import com.example.user_service.dtos.requests.RegisterRequest;
import com.example.user_service.dtos.responses.AuthResponse;
import com.example.user_service.dtos.responses.ProfilResponse;
import com.example.user_service.services.IdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Toutes les URLs de ce fichier commenceront par /api/auth
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permet au frontend de se connecter sans blocage CORS
public class AuthController {

    private final IdentityService service;



    // URL : POST http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ProfilResponse> register(@RequestBody RegisterRequest request) {
        ProfilResponse reponse = service.inscrire(request);
        return new ResponseEntity<>(reponse, HttpStatus.CREATED); // Renvoie un statut 201 Created
    }

    // URL : POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse reponse = service.connecter(request);
        return ResponseEntity.ok(reponse); // Renvoie un statut 200 OK avec le token simulé
    }
}
