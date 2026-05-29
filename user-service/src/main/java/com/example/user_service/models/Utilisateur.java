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
    @Column(name = "id", columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private String id;  // ← String au lieu de UUID

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

    private String universite;
    private String competences;

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
        if (this.id == null) {
            this.id = UUID.randomUUID().toString(); // ← génère "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
        }
        this.createdAt = LocalDateTime.now();
        if (this.role == Role.ENTREPRISE) {
            this.status = Status.EN_ATTENTE;
        } else {
            this.status = Status.ACTIF;
        }
    }
}