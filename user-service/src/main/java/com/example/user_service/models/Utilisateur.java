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
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @Column(nullable = false)
    private String telephone;

    private String photo;

    // Champs spécifiques aux étudiants
    private String universite;
    private String competences;

    // Champs spécifiques aux entreprises
    private String rccm;
    private String secteur;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();

        // Les entreprises doivent être validées manuellement
        if (this.role == Role.ENTREPRISE) {
            this.status = Status.EN_ATTENTE;
        } else {
            this.status = Status.ACTIF;
        }
    }
}