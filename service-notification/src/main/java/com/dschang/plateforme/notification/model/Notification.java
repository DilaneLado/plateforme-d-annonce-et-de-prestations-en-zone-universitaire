package com.dschang.plateforme.notification.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private String destinataireId;   // UUID de l'utilisateur
    private TypeNotification type;
    private String titre;
    private String contenu;
    private String lien;             // optionnel
    private boolean lue = false;
    private Instant createdAt = Instant.now();

    public Notification() {
    }

    // Getters et setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDestinataireId() { return destinataireId; }
    public void setDestinataireId(String destinataireId) { this.destinataireId = destinataireId; }

    public TypeNotification getType() { return type; }
    public void setType(TypeNotification type) { this.type = type; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getLien() { return lien; }
    public void setLien(String lien) { this.lien = lien; }

    public boolean isLue() { return lue; }
    public void setLue(boolean lue) { this.lue = lue; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}