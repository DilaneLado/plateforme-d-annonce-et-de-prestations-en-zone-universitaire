package com.universite.publication.dto;

import com.universite.publication.common.enums.StatutPublication;
import com.universite.publication.common.enums.TypePublication;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PublicationResponse {
    private UUID id;
    private TypePublication type;
    private String titre;
    private String description;
    private UUID auteurId;
    private String auteurNom;
    private String auteurRole;
    private String categorie;
    private List<String> medias;
    private String localisation;
    private StatutPublication statut;
    private LocalDate dateExpiration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer nbSignalements;
    private Boolean estSignalee;
}
