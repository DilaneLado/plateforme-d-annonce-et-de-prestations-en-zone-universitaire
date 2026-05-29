package com.example.user_service.repositories;

import com.example.user_service.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
    
    // Pour vérifier si l'email existe déjà lors de l'inscription
    boolean existsByEmail(String email);
    
    // Pour retrouver l'utilisateur par son email lors de la connexion
    Optional<Utilisateur> findByEmail(String email);
}