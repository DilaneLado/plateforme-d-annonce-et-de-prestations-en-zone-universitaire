package com.dschang.plateforme.notification.messaging;

public class NouveauMessageEvent {
    private String conversationId;
    private String expediteurId;
    private String destinataireId;
    private String contenu;

    // Getters et setters
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getExpediteurId() { return expediteurId; }
    public void setExpediteurId(String expediteurId) { this.expediteurId = expediteurId; }

    public String getDestinataireId() { return destinataireId; }
    public void setDestinataireId(String destinataireId) { this.destinataireId = destinataireId; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
}