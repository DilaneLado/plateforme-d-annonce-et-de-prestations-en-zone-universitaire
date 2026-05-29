package com.dschang.plateforme.notification.dto;

import com.dschang.plateforme.notification.model.TypeNotification;

import java.time.Instant;

public class NotificationResponse {
    private String id;
    private String destinataireId;
    private TypeNotification type;
    private String titre;
    private String contenu;
    private String lien;
    private boolean lue;
    private Instant createdAt;

    // Constructeur vide
    public NotificationResponse() {
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