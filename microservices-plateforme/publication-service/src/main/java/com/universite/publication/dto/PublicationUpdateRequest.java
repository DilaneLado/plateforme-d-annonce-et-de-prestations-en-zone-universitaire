package com.universite.publication.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PublicationUpdateRequest {

    @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
    private String titre;

    private String description;

    @Size(max = 100)
    private String categorie;

    private List<String> medias;

    @Size(max = 150)
    private String localisation;

    private LocalDate dateExpiration;
}
