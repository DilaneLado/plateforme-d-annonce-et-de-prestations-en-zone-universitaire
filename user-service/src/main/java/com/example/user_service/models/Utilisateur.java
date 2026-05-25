package com.example.user_service.models;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Génère automatiquement un identifiant unique UUID 
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "nom_prenom", nullable = false)
    private String nomPrenom;

    private String photo;

    // Champs spécifiques aux Étudiants [cite: 63, 142]
    private String universite;
    private String competences;

    // Champs spécifiques aux Entreprises [cite: 64]
    private String rccm; 
    private String secteur;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Cette méthode s'exécute automatiquement juste avant l'insertion en base de données 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // Enregistre la date actuelle 
        
        // Règle métier du cahier des charges : Les entreprises doivent être validées manuellement, 
        // les autres comptes (étudiants, particuliers) sont actifs immédiatement[cite: 66].
        if (this.role == Role.ENTREPRISE) {
            this.status = Status.EN_ATTENTE;
        } else {
            this.status = Status.ACTIF;
        }
    }
}
