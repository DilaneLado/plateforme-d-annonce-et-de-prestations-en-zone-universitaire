package com.dschang.plateforme.notification.messaging;

public class PublicationCreeEvent {
    private String id;
    private String type;
    private String titre;
    private String auteurId;

    // Getters et setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteurId() { return auteurId; }
    public void setAuteurId(String auteurId) { this.auteurId = auteurId; }
}