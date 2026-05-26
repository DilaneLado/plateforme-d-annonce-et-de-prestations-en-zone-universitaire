package com.universite.plateforme.administration.dto;

import com.universite.plateforme.common.enums.StatutPublication;
import jakarta.validation.constraints.NotNull;

public class ModerationRequest {

    @NotNull(message = "Le statut est obligatoire")
    private StatutPublication statut;

    private String motif;

    public ModerationRequest() {}

    public StatutPublication getStatut() { return statut; }
    public void setStatut(StatutPublication statut) { this.statut = statut; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
}
