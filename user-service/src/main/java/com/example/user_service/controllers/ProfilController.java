package com.example.user_service.controllers;

import com.example.user_service.dtos.requests.ProfilUpdateRequest;
import com.example.user_service.dtos.responses.ProfilResponse;
import com.example.user_service.services.IdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/profils") // Toutes les URLs de ce fichier commenceront par /api/profils
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfilController {

    private final IdentityService service;

    // URL : GET http://localhost:8080/api/profils/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProfilResponse> getProfil(@PathVariable String id) {
        ProfilResponse reponse = service.obtenirProfil(id);
        return ResponseEntity.ok(reponse);
    }

    // URL : PUT http://localhost:8080/api/profils/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProfilResponse> updateProfil(@PathVariable String id, @RequestBody ProfilUpdateRequest request) {
        ProfilResponse reponse = service.modifierProfil(id, request);
        return ResponseEntity.ok(reponse);
    }
}