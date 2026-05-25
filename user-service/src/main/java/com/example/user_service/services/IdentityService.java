package com.example.user_service.services;

import com.example.user_service.dtos.requests.*;
import com.example.user_service.dtos.responses.*;
import com.example.user_service.models.Utilisateur;
import com.example.user_service.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final UtilisateurRepository repository;
    
    // Le BCryptPasswordEncoder sert à hacher le mot de passe avant de le stocker en DB
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 1. INSCRIPTION
    public ProfilResponse inscrire(RegisterRequest request) {
        // On vérifie d'abord si l'email n'est pas déjà pris
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }

        // On transforme le DTO en entité Utilisateur pour la base de données
        Utilisateur utilisateur = Utilisateur.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Hachage du mot de passe
                .role(request.getRole())
                .nomPrenom(request.getNomPrenom())
                .universite(request.getUniversite())
                .competences(request.getCompetences())
                .rccm(request.getRccm())
                .secteur(request.getSecteur())
                .description(request.getDescription())
                .build();

        // On sauvegarde et on renvoie le profil créé (converti en ProfilResponse)
        Utilisateur utilisateurSauvegarde = repository.save(utilisateur);
        return mapperEnProfilResponse(utilisateurSauvegarde);
    }

    // 2. CONNEXION
    public AuthResponse connecter(LoginRequest request) {
        // On cherche l'utilisateur par son email
        Utilisateur u = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Identifiants incorrects"));

        // On vérifie si le mot de passe envoyé correspond à celui haché en base de données
        if (!passwordEncoder.matches(request.getPassword(), u.getPassword())) {
            throw new RuntimeException("Identifiants incorrects");
        }

        // Token JWT simulé (À remplacer plus tard quand on ajoutera la dépendance jjwt)
        String tokenJwtSimule = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.ExempleDeTokenUnique";

        return new AuthResponse(tokenJwtSimule, "Bearer", u.getEmail(), u.getRole().name());
    }

    // 3. RÉCUPÉRER UN PROFIL
    public ProfilResponse obtenirProfil(UUID id) {
        Utilisateur u = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return mapperEnProfilResponse(u);
    }

    // 4. MODIFIER UN PROFIL (C'est ici qu'on gère l'ajout de la photo !)
    public ProfilResponse modifierProfil(UUID id, ProfilUpdateRequest request) {
        Utilisateur u = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // On met à jour uniquement les champs envoyés par le frontend
        if (request.getNomPrenom() != null) u.setNomPrenom(request.getNomPrenom());
        if (request.getPhoto() != null) u.setPhoto(request.getPhoto()); // L'utilisateur peut ajouter sa photo ici
        if (request.getUniversite() != null) u.setUniversite(request.getUniversite());
        if (request.getCompetences() != null) u.setCompetences(request.getCompetences());
        if (request.getSecteur() != null) u.setSecteur(request.getSecteur());
        if (request.getDescription() != null) u.setDescription(request.getDescription());

        return mapperEnProfilResponse(repository.save(u));
    }

    // Méthode utilitaire interne pour transformer un "Utilisateur" en "ProfilResponse"
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