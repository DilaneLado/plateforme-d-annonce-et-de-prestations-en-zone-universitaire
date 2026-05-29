package com.example.user_service.services;

import com.example.user_service.dtos.requests.*;
import com.example.user_service.dtos.responses.*;
import com.example.user_service.models.Utilisateur;
import com.example.user_service.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final UtilisateurRepository repository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    // 1. INSCRIPTION
    public ProfilResponse inscrire(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .nomPrenom(request.getNomPrenom())
                .telephone(request.getTelephone())
                .universite(request.getUniversite())
                .competences(request.getCompetences())
                .rccm(request.getRccm())
                .secteur(request.getSecteur())
                .description(request.getDescription())
                .build();

        Utilisateur utilisateurSauvegarde = repository.save(utilisateur);
        return mapperEnProfilResponse(utilisateurSauvegarde);
    }

    // 2. CONNEXION
    public AuthResponse connecter(LoginRequest request) {
        Utilisateur u = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Identifiants incorrects"));

        if (!passwordEncoder.matches(request.getPassword(), u.getPassword())) {
            throw new RuntimeException("Identifiants incorrects");
        }

        String token = jwtService.genererToken(u);
        return new AuthResponse(token, "Bearer", u.getEmail(), u.getRole().name());
    }

    // 3. RÉCUPÉRER UN PROFIL
    public ProfilResponse obtenirProfil(String id) {  // ← String
        Utilisateur u = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return mapperEnProfilResponse(u);
    }

    // 4. MODIFIER UN PROFIL
    public ProfilResponse modifierProfil(String id, ProfilUpdateRequest request) {  // ← String
        Utilisateur u = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (request.getNomPrenom() != null) u.setNomPrenom(request.getNomPrenom());
        if (request.getPhoto() != null) u.setPhoto(request.getPhoto());
        if (request.getUniversite() != null) u.setUniversite(request.getUniversite());
        if (request.getCompetences() != null) u.setCompetences(request.getCompetences());
        if (request.getSecteur() != null) u.setSecteur(request.getSecteur());
        if (request.getDescription() != null) u.setDescription(request.getDescription());

        return mapperEnProfilResponse(repository.save(u));
    }

    // Mapper interne
    private ProfilResponse mapperEnProfilResponse(Utilisateur u) {
        return ProfilResponse.builder()
                .id(u.getId())
                .email(u.getEmail())
                .role(u.getRole())
                .nomPrenom(u.getNomPrenom())
                .photo(u.getPhoto())
                .universite(u.getUniversite())
                .competences(u.getCompetences())
                .rccm(u.getRccm())
                .secteur(u.getSecteur())
                .description(u.getDescription())
                .status(u.getStatus())
                .build();
    }
}