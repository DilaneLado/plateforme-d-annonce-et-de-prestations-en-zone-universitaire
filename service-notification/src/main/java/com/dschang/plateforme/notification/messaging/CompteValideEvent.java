package com.dschang.plateforme.notification.messaging;

public class CompteValideEvent {
    private String userId;
    private String email;
    private String nom;

    // Getters et setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}